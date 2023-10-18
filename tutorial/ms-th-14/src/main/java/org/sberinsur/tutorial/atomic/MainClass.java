package org.sberinsur.tutorial.atomic;

import java.nio.file.AtomicMoveNotSupportedException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.*;

public class MainClass {

    /**Атомарные примитивы, суть которых ясна из названия*/
    private AtomicBoolean atomicBoolean;
    private AtomicLong atomicLong;
    private final AtomicInteger counter = new AtomicInteger();

    /**Действия над любым типов в атомарном виде*/
    private AtomicReference<Integer[]> atomicReference = new AtomicReference<>();

    /**Действия над любым типов в атомарном виде + можно маркировать этот тип каким лиюо интом*/
    private AtomicStampedReference<Double>  atomicStampedReference;
    /**Действия над любым типов в атомарном виде + можно маркировать этот тип булевым значением*/
    private AtomicMarkableReference<Double> atomicMarkableReference;
    /**Экспшен при атомарном доступе к файлу (при попытке его переноса)*/
    private AtomicMoveNotSupportedException atomicMoveNotSupportedException;
    /**Атомарные действия с полем класса*/
    private AtomicIntegerFieldUpdater<MainClass> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(MainClass.class, "dopCounter");

    private Integer iCounter = 0;

    private volatile int dopCounter = 0;

    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    public int getNextInt() throws BrokenBarrierException, InterruptedException {
        cyclicBarrier.await();
        return ++iCounter;
    }

    public int getNextIntAtomic() throws BrokenBarrierException, InterruptedException {
        cyclicBarrier.await();
        return counter.incrementAndGet();
    }

    public int getNextIntAtomicField() throws BrokenBarrierException, InterruptedException {
        cyclicBarrier.await();
        return atomicIntegerFieldUpdater.incrementAndGet(this);
    }

    private void method() {
        for (int i = 0; i < 5; i++) {
            int k = i;
            new Thread(() -> {
               for (int j = 0; j < 100; j++) {
                   try {
                       System.out.println("Thread-" + k + " next int = " + getNextInt());
                   } catch (BrokenBarrierException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   try {
                       System.out.println("Thread-" + k + " next atomic int = " + getNextIntAtomic());
                   } catch (BrokenBarrierException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }).start();
        }
    }

    private void method1() {

        for (int i = 0; i < 5; i++) {
            int k = i;
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    try {
                        System.out.println("Thread-" + k + " next int = " + getNextIntAtomicField());
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        MainClass mainClass = new MainClass();
        mainClass.method();
    }
}
