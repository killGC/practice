package com.practice.solution;

import java.util.UUID;

/**
 * All rights Reserved
 * 订单与库存
 *
 * 下单涉及的步骤：
 * 1、下单
 * 2、下单同时预占库存
 * 3、支付
 * 4、支付成功真正扣减库存
 * 5、取消订单
 * 6、回退预占库存
 *
 * 什么时候预占库存：
 * 1、加入购物车时候去预占库存
 * 2、下单时候去预占库存
 * 3、支付的时候去预占库存
 * 分析：
 * 方案一：加入购物车并不代表用户一定会购买,如果这个时候开始预占库存，会导致想购买的无法加入购物车。而不想购买的人一直占用库存。显然这种做法是不可取的。
 * 方案二：商品加入购物车后，选择下单，这个时候去预占库存。用户选择去支付说明了，用户购买欲望是比 方案一 要强烈的。订单也有一个时效，例如半个小时。超过半个小时后，系统自动取消订单，回退预占库存。
 * 方案三：下单成功去支付的时候去预占库存。只有100个用户能支付成功，900个用户支付失败。用户体验不好，就像你走了一条光明大道，一路通畅，突然被告知此处不通行。而且支付流程也是一个比较复杂的流程，如果和减库存放在一起，将会变的更复杂。
 *
 * 综上所述，方案二比较合理
 *
 * 重复下单问题：
 * 1、用户点击过快，重复提交两次。
 * 2、网络延时，用户刷新或者点击下单重复提交。
 * 3、网络框架重复请求，某些网络框架，在延时比较高的情况下会自动重复请求。
 * 4、用户恶意行为。
 *
 * 解决办法：
 * 1、在UI拦截，点击后按钮置灰，不能继续点击，防止用户，连续点击造成的重复下单。
 * 2、在下单前获取一个下单的唯一token，下单的时候需要这个token。后台系统校验这个 token是否有效，才继续进行下单操作。
 *
 *
 * @Package com.practice.solution
 * @author: 长风
 * @date: 2018/10/15 下午2:09
 */
public class OrderStock {
    @Autowired
    private ReidsService redisService;

    static class User{
        private Long id;
        public Long getId(){
            return id;
        }
    }

    /**
     * 生成token放入redis
     * token 作为 key , 并设置过期时间 时间长度 根据任务需求
     * value 为数字 自增判断 是否使用过
     * @param user
     * @return
     */
    public String createToken(User user){
        String key = "placeOrder:token:"+user.getId();
        String token = UUID.randomUUID().toString();
        redisService.set(key+token,0,1000L);

        return token;
    }

    /**
     * 检查token是否有效
     * @param user
     * @param token
     * @return
     */
    public boolean checkToken(User user,String token){
        String key = "placeOrder:token:"+user.getId();
        if(null != redisService.get(key+token)){
            Long times = redisService.increment(key+token,1);
            if(times == 1){
                return true;
            }else{
                //已使用过
            }

            redisService.remove(key+token);
        }

        return false;
    }

    /**
     * 如何安全的扣减库存
     */
    interface ProductDao extends JpaRepository<Product, Integer> {

        /**
         *
         * @param pid 商品id
         * @param num 购买数量
         * @return
         */
        @Transactional
        @Modifying
        @Query("update product set availableNum=availableNum-?2,reserveNum=reserveNum+?2 where id=?1")
        int reduceStock1(Integer pid,Integer num);

        @Transactional
        @Modifying
        @Query("update product set availableNum=availableNum-?2,reserveNum=reserveNum+?2 where id=?1 and availableNum-?2>=0")
        int reduceStock2(Integer pid,Integer num);
    }

    /**
     * 下单操作
     * @param req
     * @return
     */
    private int place1(PlaceOrderReq req){
        User user = userDao.findOne(req.getUserId());
        Product product = productDao.findOne(req.getProductId());

        //下单数量
        Integer num = req.getNum();
        //可用库存
        Integer availableNum = product.getAvailableNum();

        if(availableNum>=num){
            int stock = productDao.reduceStock1(req.getProductId(),num);
            if(stock == 1){
                //生成订单
                createOrder(user,product,num);
            }else{
                logger.info("库存不足3");
            }
            return 1;
        }else{
            logger.info("库存不足4");
            return -1;
        }
    }

    private int place2(PlaceOrderReq req){
        User user = userDao.findOne(req.getUserId());
        Product product = productDao.findOne(req.getProductId());

        //下单数量
        Integer num = req.getNum();
        //可用库存
        Integer availableNum = product.getAvailableNum();

        if(availableNum>=num){
            int stock = productDao.reduceStock2(req.getProductId(),num);
            if(stock == 1){
                //生成订单
                createOrder(user,product,num);
            }else{
                logger.info("库存不足3");
            }
            return 1;
        }else{
            logger.info("库存不足4");
            return -1;
        }
    }

    @Override
    @Transactional
    public void placeOrder1(PlaceOrderReq req){
        place1(req);//不安全
    }

    /**
     在方法1 的基础上 ，更新库存的语句，增加了可用库存数量 大于 0, availableNum - num >= 0 ;
     实质是使用了数据库的乐观锁来控制库存安全，在并发量不是很大的情况下可以这么做。
     但是如果是秒杀，抢购，瞬时流量很高的话，压力会都到数据库，可能拖垮数据库。
     * @param req
     */
    @Override
    @Transactional
    public void placeOrder2(PlaceOrderReq req){
        place2(req);//安全
    }

    /**
     * 采用 Redis 锁  同一个时间只能一个请求修改同一个商品的数量
     * 缺点并发不高,同时只能一个用户抢占操作,用户体验不好！
     * 强制把处理请求串行化，缺点并发不高 ，处理比较慢，不适合抢购等方案 。
     * 用户体验也不好，明明看到库存是充足的，就是强不到。
     * 相比方案2减轻了数据库的压力。
     * @param req
     */
    @Override
    public void placeOrder3(PlaceOrderReq req){
        String lockKey = "placeOrder:"+req.getProductId;
        boolean isLock= redisService.lock(lockKey);
        if(!isLock){
            logger.info("系统繁忙稍后再试");
            return;
        }
        place1(req);//不安全
        //place2(req);

        redisService.unLock(lockKey);
    }

    /**
     * 商品的数量等其他信息先保存到Redis,并保证更新库存的时候，更新Redis。
     * 检查库存与减少库存不是原子性，以increment > 0为准
     * @param req
     */
    @Override
    public void placeOrder4(PlaceOrderReq req){
        String key = "placeOrder:"+req.getProductId;
        //先检查库存是否充足
        Integer num = redisService.get(key);
        if(num<req.getNum){
            logger.info("库存不足1");
        }else{
            //不可在这里下单减库存，否则导致数据不安全， 情况类似 placeOrder1方法；
        }

        //减少库存
        Long value = redisService.increment(key,-req.getNum().longValue());
        //库存充足
        if(value>=0){
            logger.info("抢购成功");
            //真正减扣库存、下单等操作  ,这些操作可用通过MQ或其他方式
            place2(req);
        }else{
            //库存不足，需要增加刚刚减去的库存
            redisService.increment(key,req.getNum.longValue());
            logger.info("库存不足 2");
        }

    }
}