package ru.sberinsur.tutorialProject;

/**
 * Класс, демонстрирующий способы ручного создания потоков
 * а так же демонстрация использования методов sleep(), currentThread(), start()
 * @author
 */
public class MultithreadingDemo {
    public static void main(String args[]){

        //создание потока с помощью расширения класса Thread
        Thread myThread = new MyThread();
        myThread.start();


        //создание потока с помощью имплементации интерфейса Runnable
        Thread myRunnableThread = new Thread(new MyRunnable());
        myRunnableThread.start();


        //С помощью метода sleep() можно изменить состояние потока на WAITHING:
        for (int i = 0; i < 3; i++) {
            try {
                //Статический метод переводит в режим ожидания поток, из которого вызывается
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //С помощью метода currentThread можно получить ссылку на поток, из которого вызывался метод
        //Таким образом можно взаимодействовать с потокоми.
        //Получим название потока main
        System.out.println(Thread.currentThread().getName());
    }
}
