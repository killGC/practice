package com.practice.Map;

import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved
 *
 * @Package com.practice.Map
 * @author: 长风
 * @date: 2018/10/17 下午7:03
 */
public class MapNewMethod {
    public static void main(String[] args){
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"a");
        map.put(2,"b");
        map.put(3,"c");

        String val = map.getOrDefault(4,"d");//key不存在，返回设置的默认值
        System.out.println(val+":"+map.get(4));
        System.out.println("1------------------");

        map.forEach((key,value)->System.out.println(key+":"+value));

        for(Map.Entry<Integer,String> entry :map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        System.out.println("2------------------");

        for(Integer key :map.keySet()){
            System.out.println(key+":"+map.get(key));
        }
        System.out.println("3------------------");

        String oldVal= map.putIfAbsent(4,"d");//key不存在，返回null
        System.out.println(oldVal);
        System.out.println(map.get(4));
        System.out.println("4------------------");
        oldVal= map.putIfAbsent(3,"d");//key存在，返回旧值，不用新值替换旧值
        System.out.println(oldVal);
        System.out.println(map.get(3));
        System.out.println("5------------------");

        boolean flag = map.remove(1,"b");//key关联的值与参数指定的值相等才删除
        System.out.println(flag+":"+map.get(1));
        flag = map.remove(4,"d");//已删除
        System.out.println(flag+":"+map.get(4));

        System.out.println("6------------------");

        String rplOldVal = map.replace(3,"3x");//替换key关联的值,返回替换前的值,如果key不存在，返回null
        System.out.println(rplOldVal+":"+map.get(3));

        rplOldVal = map.replace(5,"5x");
        System.out.println(rplOldVal+":"+map.get(5));
        System.out.println("7------------------");

        flag = map.replace(2,"c","2b");//key关联的值与参数指定的值相等才替换
        System.out.println(flag+":"+map.get(2));
        flag = map.replace(2,"b","2b");
        System.out.println(flag+":"+map.get(2));
        System.out.println("8------------------");

        String cptVal = map.computeIfAbsent(2,key->key+" compute");//key不存在进行计算value
        System.out.println(cptVal+":"+map.get(2));
        cptVal = map.computeIfAbsent(5,key->key+" compute");
        System.out.println(cptVal+":"+map.get(5));

        System.out.println("9------------------");

        cptVal = map.computeIfPresent(3,(key,value)->(key+1)+value);//key存在，进行计算
        System.out.println(cptVal+":"+map.get(3));
        cptVal = map.computeIfPresent(6,(key,value)->(key+1)+value);
        System.out.println(cptVal+":"+map.get(6));
        cptVal = map.computeIfPresent(3,(key,value)->null);//计算的新值为null，会把key删除
        System.out.println(cptVal+":"+map.get(3));

        System.out.println("10------------------");
        String mergeVal = map.merge(7,"e",(oldValue,newValue)->oldValue+" "+newValue);//不存在的key，把key的值不做合并放入map中
        System.out.println(mergeVal+":"+map.get(7));
        mergeVal = map.merge(7,"f",(oldValue,newValue)->oldValue+" "+newValue);//存在的key，用旧value和新value合并生成新value
        System.out.println(mergeVal+":"+map.get(7));

        System.out.println("11------------------");

        map.replaceAll((key, value) -> (key + 1) + value);//替换值
        map.forEach((key,value)->System.out.println(key+":"+value));
        System.out.println("12------------------");

    }

}
