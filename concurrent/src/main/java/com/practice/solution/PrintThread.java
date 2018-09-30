package com.practice.solution;

/**
 * All rights Reserved
 * 1、三个线程顺序打印1-100的数字
 * 2、axdjsfwersfg中找出最短 xsw字符串（该字符串每个字符之间可以有其它字符），结果xdjsfw
 * @Package com.practice.solution
 * @author: 长风
 * @date: 2018/9/25 上午9:27
 */
public class PrintThread {
    private static int i=1;
    private static Object lock = new Object();
    public static void main(String[] args){

        new Thread(new PrintRun("Thread1 :",1)).start();
        new Thread(new PrintRun("Thread2 :",2)).start();
        new Thread(new PrintRun("Thread3 :",0)).start();
    }

    static class PrintRun implements Runnable{
        private String name;
        private int remainder;
        public PrintRun(String name,int remainder){
            this.remainder = remainder;
            this.name=name;
        }

        @Override
        public void run() {
            while (i<=100){
                synchronized (lock) {
                    if (i % 3 == remainder) {
                        System.out.println(name + (i++));
                        lock.notifyAll();
                    }else {
                        try {
                            lock.wait(1000);
                            //lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
