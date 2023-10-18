package org.sberinsur.tutorial.executors;

import java.util.concurrent.*;

public class MainClass {
    private static void start(ExecutorService service) {
        for (int j = 0; j < 3; j++) {
            final int k = j;
            service.execute(() -> {
                for (int i = 0; i < 10000; i++) {
                    System.out.println("Thread " + k + ": " + i);
                }
            });
        }
    }

    public static void main(String[] args) {
        start(Executors.newCachedThreadPool());
//        start(Executors.newSingleThreadExecutor());
//        start(Executors.newFixedThreadPool(2));
//        start(Executors.newWorkStealingPool());
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        for (int j = 0; j < 3; j++) {
            final int k = j;
            service.scheduleWithFixedDelay(() -> {
                for (int i = 0; i < 10000; i++) {
                    System.out.println("Thread " + k + ": " + i);
                };
            }, 10L, 10L, TimeUnit.SECONDS);
        }
    }
}
