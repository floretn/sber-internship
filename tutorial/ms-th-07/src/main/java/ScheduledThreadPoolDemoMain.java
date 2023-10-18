import java.time.LocalTime;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class ScheduledThreadPoolDemoMain {
    public static void main(String[] args){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
        //Выполнение задачи без вовзращаемого значения
        executor.schedule(() ->{
            System.out.println("schedule " + System.currentTimeMillis());
        }, 5, TimeUnit.SECONDS);

        //Выполнение задаи с возвращаемым значением
        ScheduledFuture<String> future = executor.schedule(() ->{
            System.out.println("scheduled future " + System.currentTimeMillis());
            return "тута возвращаемое значение";
        }, 5, TimeUnit.SECONDS);

        //Получаем возвращаемое значение
        try {
            System.out.println(future.get() + System.currentTimeMillis());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //Выполняем задачу с фиксированной частотой, каждые 2 секунды и через 1 секунду
        //через 2 секунды после старта задачи
        executor.scheduleAtFixedRate(() -> {
            System.out.println("at Fixed Rate" + System.currentTimeMillis());
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        }, 1, 2, TimeUnit.SECONDS);

        //Выполняем задачу с фиксированной задержкой, выполняем каждые 2 секунды и через одну секунду
        //Через 2 секунды после завершения задачи
        executor.scheduleWithFixedDelay(() -> {
            System.out.println("with Fixed Delay" + System.currentTimeMillis());
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        }, 1, 2, TimeUnit.SECONDS);
    }



    //ScheduedFutureTaskб метод определения задержки.
    /*public long getDelay(TimeUnit unit) {
        // Время выполнения минус текущее системное время
        return unit.convert(time - now(), NANOSECONDS);
    }*/

    /** Для отмены задачи достаточно бросить исключение, либо добавить задачу, которая отменет повторющуюся*/
    /*public static void main(String[] args) throws InterruptedException, ExecutionException {
        Создвкс пул потоков
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> future = pool.scheduleAtFixedRate(() -> {
            System.out.format("%s: %s%n", LocalTime.now(), Thread.currentThread().getName());
        }, 2, 5, TimeUnit.SECONDS);
        pool.schedule(() -> {
            System.out.println("Cancel fixed rate task and shutdown");
            future.cancel(true);
            pool.shutdown();
        }, 20, TimeUnit.SECONDS);
    }*/
}

