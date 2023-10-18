import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemoMain {
    public static void main(String[] args) {
        try {
            System.out.println(timeTasks(3, new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 3; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Первая форма метода await, ожидание длится до тех пор, пока отсчет, связанный с вызывающим объектом типа
    //CountDownLatch не достигнет нуля.
//void await() throws InterruptedException
    //Вторая форма, ожидание длится только в течение определенооого времени, определяемого вторым аргументом
    //true - если обратный отсчет достиг нуля
//boolean await(long ожидание, TimeUnit единица_времени) throws InterruptedException
    //Метод, который уменьшает счетчик на единицу, извещая о том, что событие произашло
//void countDown()
    //Возвращает количество оставшихся событий
//long getCount()
    //@Override, возвращает красивую строку с количеством оставшихся событий
//String toString()

    public static double timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        //Здесь рабочие потоки выполняют ожидание на стартовом завторе
                        startGate.await();
                        try {
                            System.out.println(Thread.currentThread().getName() + " начал работу");
                            task.run();
                        } finally {
                            //Здесь мы уменьшаем счетчик на конечном затворе
                            System.out.println(Thread.currentThread().getName() + " вызываю countDown для конечной защелки");
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {}
                }
            };
            t.start();
        }
        long start = System.nanoTime();
        //Здесь мы уменьшаем счетчик после события запуска всех потоков в цикле,
        //соответсвенно потоки отпускаются на выполнение
        System.out.println(startGate + " " + "до запуска всех потоков потоков");
        startGate.countDown();
        System.out.println(startGate + " " + "после запуска всех потоков");
        //Здесь мы ожидаем конец выполнения каждого из потоков
        endGate.await();
        long end = System.nanoTime();
        return (end-start) / 100000.0 / 10000;
    }
    // Если бы у нас была реализована многопоточка иначе, то
    // На нашем проекте это можно было  прменить, допустим, при ожидании загрузки файла, после 5 попыток ожидания,
    // файл отпускается и передается следующему потоку-обработчику гарантированно после всего ожидания
}
