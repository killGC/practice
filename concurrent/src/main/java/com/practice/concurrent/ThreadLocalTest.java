package com.practice.concurrent;

/**
 * All rights Reserved
 *
 * @Package com.practice.concurrent
 * @author: 长风
 * @date: 2018/9/25 下午7:26
 */
public class ThreadLocalTest {

    public static void main(String[] args){
        ThreadLocal local = new ThreadLocal();
        local.set("test");

        System.out.println(Thread.currentThread().getName()+":"+local.get());

        new Thread(new RunSet(local)).start();
        new Thread(new RunPrint(local)).start();
        new Thread(new RunSetPrint(local)).start();

    }

    static class RunPrint implements Runnable{
        private ThreadLocal local;

        public RunPrint(ThreadLocal local){
            this.local = local;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println(Thread.currentThread().getName()+":"+local.get());
                try {
                    Thread.sleep(1000);
                }catch (Exception ex){

                }
            }
        }
    }

    static class RunSet implements Runnable{
        private ThreadLocal local;

        public RunSet(ThreadLocal local){
            this.local = local;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println(Thread.currentThread().getName()+":");
                local.set("set");
                try {
                    Thread.sleep(1000);
                }catch (Exception ex){

                }
            }
        }
    }

    static class RunSetPrint implements Runnable{
        private ThreadLocal local;

        public RunSetPrint(ThreadLocal local){
            this.local = local;
        }

        @Override
        public void run() {
            this.local.set("setPrint");
            while (true) {
                System.out.println(Thread.currentThread().getName()+":"+local.get());
                try {
                    Thread.sleep(1000);
                }catch (Exception ex){

                }
            }
        }
    }
}
