package com.practice.concurrent;

import java.util.Random;
import java.util.concurrent.*;

/**
 * All rights Reserved
 * forkjoin 大任务分解为小任务 fork拆分成子任务 join合并子任务结果
 * RecursiveAction没有返回值的任务
 * RecursiveTask有返回值的任务
 * @Package com.practice.concurrent
 * @author: 长风
 * @date: 2018/9/5 下午6:58
 */
public class ForkJoin {
    public static void main(String[] args) throws Exception{

        //doRecursiveAction();
        doRecursiveTask();

    }

    private static void doRecursiveAction() throws Exception{
        RecursiveActionDemo demo = new RecursiveActionDemo(0,1000);

        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(demo);

        pool.awaitTermination(2, TimeUnit.SECONDS);

        pool.shutdown();
    }

    private static void doRecursiveTask() throws Exception{
        int arr[] = new int[1000];
        Random random = new Random();
        int total = 0;
        // 初始化100个数字元素
        for (int i = 0; i < arr.length; i++) {
            int temp = random.nextInt(100);
            // 对数组元素赋值,并将数组元素的值添加到total总和中
            total += (arr[i] = temp);
        }
        System.out.println("初始化时的总和=" + total);

        // 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 提交可分解的PrintTask任务
        //Future<Integer> future = forkJoinPool.submit(new RecursiveTaskDemo(arr, 0, arr.length));
        //System.out.println("计算出来的总和="+future.get());


        Integer integer = forkJoinPool.invoke( new RecursiveTaskDemo(arr, 0, arr.length)  );
        System.out.println("计算出来的总和=" + integer);

        // 关闭线程池
        forkJoinPool.shutdown();
    }

    static class RecursiveActionDemo extends RecursiveAction{
        private static final int max = 20;

        private int start;
        private int end;

        public RecursiveActionDemo(int start,int end){
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if(end -start<max){
                int sum = 0;
                for(int k=start;k<end;k++){
                    sum +=k;
                }
                System.out.println("start "+start+" end "+ end);
                System.out.println(""+Thread.currentThread().getName()+" sum "+sum);
            }else{
                int middle = (start+end)/2;


                RecursiveActionDemo left = new RecursiveActionDemo(start,middle);
                RecursiveActionDemo right = new RecursiveActionDemo(middle,end);

                left.fork();
                right.fork();
            }
        }
    }

    static class RecursiveTaskDemo extends RecursiveTask<Integer>{
        private int start;
        private int end;
        private int[] arr;

        private static final int MAX=70;

        public RecursiveTaskDemo(int[] arr,int start,int end){
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            if(end-start<MAX){
                for(int k=start;k<end;k++){
                    sum += arr[k];
                }

                return sum;
            }else{
                int middle = (start+end)/2;
                RecursiveTaskDemo left = new RecursiveTaskDemo(arr,start,middle);
                RecursiveTaskDemo right = new RecursiveTaskDemo(arr,middle,end);

                left.fork();
                right.fork();

                return left.join() + right.join();
            }
        }
    }
}
