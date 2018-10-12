package com.practice.thread;

/**
 * All rights Reserved
 *
 * @Package com.practice.thread
 * @author: 长风
 * @date: 2018/10/12 下午4:27
 */


public class RunnableJoin {

    static class CustomRunnable1 implements Runnable{
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

    static class CustomRunnable implements Runnable{
        private Thread t1;
        public CustomRunnable(Thread t1){
            this.t1 = t1;
        }
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " start.");

            try {
                t1.join();

                System.out.println(threadName + " end.");
            } catch (Exception ex) {
                System.out.println("Exception from " + threadName + " run.");
            }
        }
    }

    public static void main(String[] args){
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName+" start.");

        Thread thread1 = new Thread(new CustomRunnable1(),"Thread1");
        Thread thread = new Thread(new CustomRunnable(thread1),"Thread");

        try {
            thread1.start();
            Thread.sleep(2000);
            thread.start();
            thread.join();
        }catch (Exception ex){
            System.out.println("Exception from main");
        }

        System.out.println(threadName+" end.");
    }
}
