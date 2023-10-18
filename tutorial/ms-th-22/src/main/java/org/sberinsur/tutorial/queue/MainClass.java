package org.sberinsur.tutorial.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        /*
        * Строго ограничена в размерах.
        * Основана на массиве определённой длины (которая задаётся при создании).
        * Можно задать способы блокировки потоков (с сохранением потоков в том порядке,
        *   в котором они пришли, или нет).
        * Имеет единую блокировку на чтение/запись.
        */
        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(5);
        arrayBlockingQueue.add(0);
        arrayBlockingQueue.add(1);
        arrayBlockingQueue.add(2);
        arrayBlockingQueue.add(3);
        arrayBlockingQueue.add(4);
        //System.out.println(arrayBlockingQueue.peek());
        System.out.println(arrayBlockingQueue.poll());
        arrayBlockingQueue.add(5);
        System.out.println(arrayBlockingQueue);
        /*
         * Ограничена в размерах при желании, иначе не ограничена.
         * Основана на связном списке переменно длины (которую можно ограничить при создании).
         * Нельзя задать способы блокировки потоков.
         * Имеет две разные блокировки на (чтение-удаление)/запись.
         */
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        /*
         * Очередь хранит в себе элементы, которые становятся видны только спустя определённое время.
         * см. DelayInteger
         */
        DelayQueue<DelayInteger> delayQueue = new DelayQueue<>();
        delayQueue.add(new DelayInteger(1, 10000L));
        System.out.println("Начало");
        System.out.println(delayQueue.poll());
        Thread.sleep(15000);
        System.out.println(delayQueue.poll().i);
    }
}
