package com.practice.thread;

/**
 * All rights Reserved
 * Thread.join方法：
 * 1、一个线程等待另外一个线程执行完成
 * 2、需要等待另外一个线程的执行结果
 * 3、如果线程被生成了，但还未被起动，调用它的join()方法是没有作用的
 * @Package com.practice.thread
 * @author: 长风
 * @date: 2018/10/12 下午3:27
 */
public class ThreadJoin {
    static class CustomThread1 extends Thread{
        public CustomThread1(String threadName){
            super(threadName);
        }

        public void run(){
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName+" start.");

            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println(threadName + " loop at " + i);
                    Thread.sleep(2000);
                }

                System.out.println(threadName+" end.");
            }catch (Exception ex){
                System.out.println("Exception from " +threadName+" run.");
            }
        }
    }

    static class CustomThread extends Thread{
        private CustomThread1 t1;

        public CustomThread(String threadName,CustomThread1 customThread1){
            super(threadName);
            t1 = customThread1;
        }

        public void run(){
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName+" start.");

            try {
                t1.join();

                System.out.println(threadName+" end.");
            }catch (Exception ex){
                System.out.println("Exception from " +threadName+" run.");
            }
        }
    }

    public static void main(String[] args){
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName+" start.");

        CustomThread1 customThread1 = new CustomThread1("CustomThread1");
        CustomThread customThread = new CustomThread("customThread",customThread1);

        try {
            customThread1.start();
            Thread.sleep(2000);
            customThread.start();
            customThread.join();
        }catch (Exception ex){
            System.out.println("Exception from main");
        }

        System.out.println(threadName+" end.");
    }
}
