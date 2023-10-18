package ru.sberisnur.tutorial.completableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CFRunDemoMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Итак, простейший пример CF
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(()-> "Сообщение");
        //Чтобы получить результат - один из способов вызвать get(), унаследованный от Future
        String result = stringCompletableFuture.get();
        //Однако можно завершить CF ручками: - в данной ситуации игнорируется.
        stringCompletableFuture.complete("Результат Future");


        //--------------------------------------------------------------------------------------------------------------
        //Выполнение асинхронных задач:
        // Асинхронно запускаем задачу, заданную объектом Runnable, не возвращает результат---
        // Использование лямбда-выражения
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Имитация длительной работы
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Я буду работать в отдельном потоке, а не в главном.");
        });
        // Блокировка и ожидание завершения Future
        System.out.println(future.get());

        // Запуск асинхронной задачи, заданной объектом Supplier
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Результат асинхронной задачи";
        });
        // Блокировка и получение результата Future
        String result1 = future1.get();
        System.out.println(result);

    }
}
