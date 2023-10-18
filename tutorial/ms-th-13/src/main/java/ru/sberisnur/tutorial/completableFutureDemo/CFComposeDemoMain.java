package ru.sberisnur.tutorial.completableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CFComposeDemoMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * Комбинирование двух зависимых задач с использованием thenCompose
         */
        /*CompletableFuture<User> getUsersDetail(String userId) {
            return CompletableFuture.supplyAsync(() -> {
                UserService.getUserDetails(userId);
            });
        }

        CompletableFuture<Double> getCreditRating(User user) {
            return CompletableFuture.supplyAsync(() -> {
                CreditRatingService.getCreditRating(user);
            });
        }
        // Так делать не надо, ведь в таком случае у нас получается вложенный CF
        CompletableFuture<CompletableFuture<Double>> result = getUserDetail(userId)
            .thenApply(user -> getCreditRating(user));

        // Так делать нужно, избавляемся от вложенности
        CompletableFuture<Double> result = getUserDetail(userId)
            .thenCompose(user -> getCreditRating(user));

        */

        /**
         *  КОмбинирование двух независимых задач, с использованием thenCombine
         */
        System.out.println("Получение веса.");
        CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 65.0;
        });

        System.out.println("Получение роста.");
        CompletableFuture<Double> heightInCmFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 177.8;
        });

        System.out.println("Расчёт индекса массы тела.");
        CompletableFuture<Double> combinedFuture = weightInKgFuture
                .thenCombine(heightInCmFuture, (weightInKg, heightInCm) -> {
                    Double heightInMeter = heightInCm / 100;
                    return weightInKg/(heightInMeter * heightInMeter);
                });

        System.out.println("Ваш индекс массы тела - " + combinedFuture.get());


    }
}
