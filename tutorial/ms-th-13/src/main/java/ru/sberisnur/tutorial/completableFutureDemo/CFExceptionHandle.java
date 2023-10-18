package ru.sberisnur.tutorial.completableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CFExceptionHandle {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * Обработка исключений - exceptionally()
         */
        Integer age = -1;

        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if (age < 0) {
                throw new IllegalArgumentException("Возраст не может быть отрицательным");
            }
            if (age > 18) {
                return "Взрослый";
            } else {
                return "Ребёнок";
            }
        }).exceptionally(ex -> {
            System.out.println("Ой! У нас тут исключение - " + ex.getMessage());
            return "Неизвестно!";
        });
        System.out.println("Зрелость: " + maturityFuture.get());


        /**
         *  Обработка ошибок, метод handle()
         */
        Integer age1 = -1;

        CompletableFuture<String> maturityFuture1 = CompletableFuture.supplyAsync(() -> {
            if (age1 < 0) {
                throw new IllegalArgumentException("Возраст не может быть отрицательным");
            }
            if (age1 > 18) {
                return "Взрослый";
            } else {
                return "Ребёнок";
            }
        }).handle((res, ex) -> {
            if (ex != null) {
                System.out.println("Ой! У нас тут исключение - " + ex.getMessage());
                return "Неизвестно!";
            }
            return res;
        });

        System.out.println("Зрелость: " + maturityFuture1.get());
    }
}
