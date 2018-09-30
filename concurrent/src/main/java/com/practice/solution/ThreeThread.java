package com.practice.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class ThreeThread {
    public static void main(String[] args){
        int size = 100/3;
        List list1 = new ArrayList(size+1);
        List list2 = new ArrayList(size);
        List list3 = new ArrayList(size);
        for(int k=1;k<=100;k++){
            if(k%3==1){
                list1.add(k);
            }else if(k%3==2){
                list2.add(k);
            }else if(k%3==0){
                list3.add(k);
            }
        }
        System.out.println(list1.toString());
        System.out.println(list2.toString());
        System.out.println(list3.toString());

        AtomicInteger signal = new AtomicInteger(1);

        Semaphore semaphore = new Semaphore(1);

        Thread thread1 = new Thread(new PrintThread(semaphore,signal,"Thread1: ",list1));
        Thread thread2 = new Thread(new PrintThread (semaphore,signal,"Thread2: ",list2));
        Thread thread3 = new Thread(new PrintThread(semaphore,signal,"Thread3: ",list3));

        thread1.start();
        thread2.start();
        thread3.start();
    }

    static class PrintThread implements Runnable{
        private Semaphore semaphore;
        private volatile AtomicInteger signal;
        private String name;
        private List list;

        public PrintThread(Semaphore semaphore,AtomicInteger signal,String name,List list){
            this.semaphore = semaphore;
            this.signal = signal;
            this.name = name;
            this.list = list;
        }

        @Override
        public void run() {
            for(int k=0;k< list.size();k++){
                try {
                    int data = (Integer) list.get(k);
                    int update = data + 1;

                    for(;;) {
                        semaphore.acquire();
                        if (signal.compareAndSet(data, update)) {

                            System.out.println(name + data);
                            semaphore.release();
                            break;
                        }
                        semaphore.release();
                    }
                }catch (Exception ex){

                }
            }

        }
    }
}
