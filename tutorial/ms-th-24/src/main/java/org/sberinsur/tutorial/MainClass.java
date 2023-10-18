package org.sberinsur.tutorial;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class MainClass {

    public static PriorityBlockingQueue<Integer> priorityBlockingQueue =
            new PriorityBlockingQueue<>(3, (x, y) -> Integer.compare(y, x));

//    public static PriorityBlockingQueue<Integer> priorityBlockingQueue =
//            new PriorityBlockingQueue<>(3);

    public static void main(String[] args) throws InterruptedException {
        /*
        * Очередь с приоритетами. Приоритеты выставляются на основании переданного экземпляра
        * Comparable. Если этот экземпляр передан не был, сохраняются в порядке добавления.
        * Одна общая блокировка на методы добавления и удаления.
        */
        priorityBlockingQueue.add(1);
        priorityBlockingQueue.add(3);
        System.out.println(priorityBlockingQueue);
        priorityBlockingQueue.add(2);
        priorityBlockingQueue.add(10);
        System.out.println(priorityBlockingQueue);
        System.out.println(priorityBlockingQueue.poll());
        System.out.println(priorityBlockingQueue.poll());
        System.out.println(priorityBlockingQueue.poll());
        System.out.println(priorityBlockingQueue.poll());
        System.out.println(priorityBlockingQueue);

        System.out.println("************************************************************************************");

        new Thread(() -> {
            try {
                int i = priorityBlockingQueue.take();
                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(4000);
        System.out.println("Ввожу число");
        priorityBlockingQueue.add(12);

        System.out.println("************************************************************************************");

        /*
        * Синхронизированная очередь.
        * Любая операция вставки должна выполняться одновременно с операцией взятия объекта.
        * И наоборот.
        * Можно выставить сохренение очерёдности ждущих потоков или нет (флагом в конструкторе).
        */
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>(false);
        //        System.out.println(synchronousQueue.poll());
        new Thread(() -> {
            System.out.println(synchronousQueue.poll());
            try {
                System.out.println(synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(4000);
        synchronousQueue.put(1);
    }
}
