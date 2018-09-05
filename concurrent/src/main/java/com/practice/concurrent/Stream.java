package com.practice.concurrent;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

/**
 * All rights Reserved
 *
 * @Package com.practice.concurrent
 * @author: 长风
 * @date: 2018/9/5 下午5:28
 */
public class Stream {
    private static final int CYCLE=100000;

    public static void main(String[] args){
        cal("sequential");
        cal("parallel");
        sumByFor();
    }

    /**
     * <p>java 8 forkjoin </p>
     * @param type
     * @return void
     * @throws
     * @author changfeng
     * @date 2018/9/5 下午6:00
     * @since V1.1.0-SNAPSHOT
     *
     */
    private static void cal(String type){
        Instant start = Instant.now();

        Long result;
        if("parallel".equals(type)) {
            result = LongStream.rangeClosed(0, CYCLE)
                    .parallel()
                    .reduce(0, Long::sum);
        }else{

            result = LongStream.rangeClosed(0,CYCLE)
                    .sequential()
                    .reduce(0,Long::sum);
        }

        Instant end = Instant.now();

        System.out.println(type+" 耗费时间"+Duration.between(start,end).toMillis());
        System.out.println(type+" 结果 "+result);
        System.out.println("=========================");
    }

    private static void sumByFor(){
        Instant start = Instant.now();

        Long total = 0L;
        for(Long k=0L;k<=CYCLE;k++){
            total+=k;
        }
        Instant end = Instant.now();
        System.out.println("sumByFor 耗费时间 "+Duration.between(start,end).toMillis());

        System.out.println("sumByFor 结果 "+total);
        System.out.println("=========================");
    }
}
