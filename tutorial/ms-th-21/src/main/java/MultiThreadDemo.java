import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;


public class MultiThreadDemo {
    public static ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

    public static CountDownLatch StartGate = new CountDownLatch(1);
    public static CountDownLatch EndGate = new CountDownLatch(3);


    public static void main(String[] args) {
        new Thread(new PutThread()).start();
        new Thread(new GetThread()).start();
        new Thread(new ReplaceThread()).start();
        StartGate.countDown();
        try {
            EndGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(concurrentHashMap.size());
        System.out.println(concurrentHashMap);
    }
}

class PutThread implements Runnable{
    @Override
    public void run() {
        try {
            MultiThreadDemo.StartGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MultiThreadDemo.concurrentHashMap.putIfAbsent(i,i);
        }
        MultiThreadDemo.EndGate.countDown();
    }
}

class GetThread implements Runnable {
    @Override
    public void run() {
        try {
            MultiThreadDemo.StartGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int sumOfValues = 0;
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sumOfValues = sumOfValues + MultiThreadDemo.concurrentHashMap.getOrDefault(i,0);
        }
        MultiThreadDemo.EndGate.countDown();
        System.out.println(sumOfValues);
    }
}

class ReplaceThread implements Runnable{
    @Override
    public void run() {
        try {
            MultiThreadDemo.StartGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MultiThreadDemo.StartGate.countDown();
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MultiThreadDemo.concurrentHashMap.replace(i, i, i+1);
        }
        MultiThreadDemo.EndGate.countDown();
    }
}



