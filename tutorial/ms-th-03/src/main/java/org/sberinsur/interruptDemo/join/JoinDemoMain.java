package org.sberinsur.interruptDemo.join;

public class JoinDemoMain {
    public static void main(String[] args){
        Thread thread1 = new JoinDemoThread("thread1");
        Thread thread2 = new JoinDemoThread("thread2");
        thread1.start();
        try {
            System.out.println(Thread.currentThread().getState());
            thread1.join();
            System.out.println(thread2.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(thread1.getState() + " тот самый статус в конце работы " + thread1.getName());

        thread2.start();
    }
}
