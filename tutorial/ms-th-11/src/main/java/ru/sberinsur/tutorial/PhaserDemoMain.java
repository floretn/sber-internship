package ru.sberinsur.tutorial;

import java.util.concurrent.Phaser;

public class PhaserDemoMain {
    public static void main(String[] args) {

        //Объявляем этапщик с количеством участинокв 1
        Phaser phaser = new Phaser(3);
        new Thread(new PhaseThread(phaser, "PhaseThread 1")).start();
        new Thread(new PhaseThread(phaser, "PhaseThread 2")).start();

        // ждем завершения фазы 0
        int phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");
        // ждем завершения фазы 1
        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        // ждем завершения фазы 2
        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        phaser.arriveAndDeregister();
    }
}
    /*Phaser()
    Phaser(int parties)
    Phaser(Phaser parent)
    Phaser(Phaser parent, int parties)*/


//int register() - регистрирует сторону, которая выполняет фазы, и возвращает номер текущей фазы - обычно 0
//int arrive() - сообщает, что сторона завершила фазу и возвращает номер текущей фазы
//int arriveAndAwaitAdvance() - аналогичен методу arrive, только при этом заставляет phaser ожидать завершения фазы всеми остальными сторонами
//int arriveAndDeregister() - сообщает о завершении всех фаз стороной и снимает ее с регестрации. ВОзвращает номер текущей фазы или отрицательное число, если синхронизатор Phaser завершил свою работу
//int getPhase() = возвращает номер текущей фазы

class PhaseThread implements Runnable{

    Phaser phaser;
    String name;

    PhaseThread(Phaser p, String n){

        this.phaser=p;
        this.name=n;
        //Регестрируемся

    }
    public void run(){

        System.out.println(name + " выполняет фазу " + phaser.getPhase());
        phaser.arriveAndAwaitAdvance(); // сообщаем, что первая фаза достигнута
        try{
            Thread.sleep(200);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }

        System.out.println(name + " выполняет фазу " + phaser.getPhase());
        phaser.arriveAndAwaitAdvance(); // сообщаем, что вторая фаза достигнута
        try{
            Thread.sleep(200);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println(name + " выполняет фазу " + phaser.getPhase());
        phaser.arriveAndDeregister(); // сообщаем о завершении фаз и удаляем с регистрации объекты
    }
}