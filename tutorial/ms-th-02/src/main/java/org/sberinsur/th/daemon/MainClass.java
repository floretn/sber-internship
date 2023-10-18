package org.sberinsur.th.daemon;

public class MainClass {
    public static void main(String[] args) {
        Thread daemon = new Thread(() -> {
            Thread daemonInner = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    System.out.println("Inner Daemon: " + i);
                }
            });
            daemonInner.start();
            for (int i = 0; i < 1000; i++) {
                System.out.println("Daemon: " + i);
            }
        });
        daemon.setDaemon(true);

        Thread notDaemon = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Thread.yield();
                System.out.println("Not Daemon: " + i);
            }
            System.out.println("Hello world!");
        });
        daemon.start();
        notDaemon.start();

    }
}
