package ru.sberinsur.tutorialProject;

/**
 * Ручное создание потока с помощью наследования класса Thread
 * Поток, выводящий числа от 0 до 100
 * @author ioromanov
 */
public class MyThread extends Thread{

    @Override
    public void run(){
        for (int i = 0; i < 10 ; i++) {
            System.out.println("sout from MyThread thread" + i);
        }
    }
}
