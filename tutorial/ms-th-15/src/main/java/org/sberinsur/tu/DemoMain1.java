package org.sberinsur.tu;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class DemoMain1 {

    private static AtomicIntegerArray num= new AtomicIntegerArray(2);

    public static void main(String[] args) throws InterruptedException {
            Thread t1 = new Thread(new MyRun1());
            Thread t2 = new Thread(new MyRun2());

            num.set(0, 10);
            num.set(1, 0);

            System.out.println("In Main num before:"+num.get(1));

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("In Main num after:"+num.get(1));
        }

        static class MyRun1 implements Runnable {
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    num.set(1,num.get(1)+1);
                    //num.accumulateAndGet(1, 0, (x, d)->x+d);
                }

            }
        }

        static class MyRun2 implements Runnable {
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    num.set(1,num.get(1)+1);
                    //num.accumulateAndGet(1, 1, (x, d)->x+d);
                }

            }

        }

    }