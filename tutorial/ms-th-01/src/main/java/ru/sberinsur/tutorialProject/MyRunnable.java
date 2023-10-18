package ru.sberinsur.tutorialProject;

/**
 * Ручно создание потока с помощью интерефейса Runnable
 * Поток, выводящий в консоль числа от 0 до 100
 * @author ioromanov
 */
public class MyRunnable implements Runnable{
    @Override
    public void run(){
        for (int i = 0; i < 10; i++) {
            System.out.println("sout from MyRunnable thread: "  + i);
        }
    }
}
