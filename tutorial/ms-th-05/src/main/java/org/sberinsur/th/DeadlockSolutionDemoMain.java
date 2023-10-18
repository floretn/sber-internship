package org.sberinsur.th;

import java.util.ArrayList;
import java.util.List;

public class DeadlockSolutionDemoMain {
    public  static void main(String[] args){
        List<String> x = new ArrayList<>();
        List<String> y = new ArrayList<>();

        new Thread("xThread"){
            public void run(){
                synchronized (x) {
                    System.out.println(Thread.currentThread().getName() + " Захватил монитор x");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    x.add("Запись в список из поток x " + Thread.currentThread().getName());
                    System.out.println(Thread.currentThread().getName() + " Пытаюсь захватить монитор y");
                    x.notify();
                }
                synchronized (y) {
                    System.out.println(Thread.currentThread().getName() + " Захватил монитор y");
                    try {
                        y.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    y.add("Запись в список из поток x " + Thread.currentThread().getName());
                }
            }
        }.start();

        new Thread("yThread"){
            public void run(){
                synchronized (y) {
                    System.out.println(Thread.currentThread().getName() + " Захватил монитор y");
                    try {
                        Thread.sleep(1000);
                        y.add("Запись в список из поток x " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " Пытаюсь захватить монитор x");
                    y.notify();
                }
                synchronized (x) {
                    System.out.println(Thread.currentThread().getName() + " Захватил монитор x");
                    try {
                        x.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    x.add("Запись в список из поток x " + Thread.currentThread().getName());
                }
            }
        }.start();
    }
}
