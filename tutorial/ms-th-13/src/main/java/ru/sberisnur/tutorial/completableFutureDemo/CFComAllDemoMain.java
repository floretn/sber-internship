package ru.sberisnur.tutorial.completableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CFComAllDemoMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * Объединение нескольких CF
         */
        //static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)
        //static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)

        CompletableFuture<String> what = CompletableFuture.supplyAsync(() -> "What");
        CompletableFuture<String> the = CompletableFuture.supplyAsync(() -> "the");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "future");
        CompletableFuture<String> holds = CompletableFuture.supplyAsync(() -> "holds");

        CompletableFuture<Void> all = CompletableFuture.allOf(what, the, future, holds);

        String result = Stream.of(what, the, future, holds)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));
        System.out.println(result);

        /**
         * Гонка результатов CF anyOf()
         */
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Результат Future 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Результат Future 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Результат Future 3";
        });

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2, future3);

        System.out.println(anyOfFuture.get()); // Результат Future 2

    }
}
