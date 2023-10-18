package org.sberinsur.tutorial.feature;

import java.util.LinkedList;
import java.util.concurrent.*;

public class MainClass {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Integer> future = new Future<>() {

            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Integer get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Integer get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };

        ExecutorService service = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        Callable<Integer> callable = () -> {Thread.sleep(1000); return 1;};
        future = service.submit(callable);
        System.out.println(future.isDone());
        //future.cancel(true);
        System.out.println(future.get());
        System.out.println(future.isDone());

        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return 1;
        });

        service.submit(futureTask);
        System.out.println(futureTask.isDone());
        System.out.println(futureTask.get());

        FutureTask<Integer> futureTask1 = new FutureTask<>(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 100);

        //service.execute(futureTask1);
        System.out.println(futureTask1.isDone());
        System.out.println(futureTask1.get());
        System.out.println("Da");
    }
}
