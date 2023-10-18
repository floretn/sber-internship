package org.sberinsur.tu;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class DemoMain {

    static final AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(100);

    public static void main(String[] args) {

        for (int i = 0; i<atomicIntegerArray.length(); i++) {
            atomicIntegerArray.set(i, 1);
        }


        Thread t1 = new Thread(new Increment());
        Thread t2 = new Thread(new Compare());
        t1.start();
        t2.start();



        System.out.println("Values: ");

        for (int i = 0; i<atomicIntegerArray.length(); i++) {
            System.out.print(atomicIntegerArray.get(i) + " ");
        }
    }

    static class Increment implements Runnable {

        public void run() {

            for(int i = 0; i<atomicIntegerArray.length(); i++) {
                int add = atomicIntegerArray.incrementAndGet(i);
                System.out.println("Thread " + Thread.currentThread().getId()
                        + ", index " +i + ", value: "+ add);
            }
        }
    }

    static class Compare implements Runnable {

        public void run() {

            for(int i = 0; i<atomicIntegerArray.length(); i++) {
                boolean swapped = atomicIntegerArray.compareAndSet(i, 2, 3);

                if(swapped) {
                    System.out.println("Thread " + Thread.currentThread().getId()
                            + ", index " +i + ", value: 3");
                }
            }
        }
    }
}