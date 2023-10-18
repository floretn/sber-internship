package ru.sberisnur.tutorial.completableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CFCallBackDemoMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * Возвращаем результат из CF ----------------------------------------------------------------------------------
         */
        // Создаём CompletableFuture
        CompletableFuture<String> whatsYourNameFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Илья";
        });
        // Добавляем колбэк в CF, используя thenApply()
        /*
        В качестве аргумента он принимает Function<T, R>.
        Function<T, R> это тоже функциональный интерфейс, представляющий функцию,
        которая принимает аргумент типа T и возвращает результат типа R:
        */
        CompletableFuture<String> greetingFuture = whatsYourNameFuture.thenApply(name -> {
            return "Привет," + name;
        });
        // Блокировка и получение результата Future
        System.out.println(greetingFuture.get()); // Привет, Илья
        //-----------------------------------------------------------------------------------------------------------------------------------------------------

        //Мы так же можем сделать несколько последовательных преобразований, используя серию
        //вызовов thenApply(). Результат последоватеьно передается от предыдущего к следуюущему.
        CompletableFuture<String> welcomeText = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Илья";
        }).thenApply(name -> {
            return "Привет," + name;
        }).thenApply(greeting -> {
            return greeting + ". Добро пожаловать в Сбер";
        });
        System.out.println(welcomeText.get());
        // Выводит: Привет, Илья. Добро пожаловать в Сбер

        /**
         * Без возвращаемого значения ----------------------------------------------------------------------------------
         */
        // Пример thenAccept()
       /*
        CompletableFuture.supplyAsync(() -> {
            //Что-то вернулось
        }).thenAccept(product -> {       <---- Имеет доступ к результату CF
            //После этого выполняем какие-то действия
        });
        */

        /* Пример thenRun()
        CompletableFuture.supplyAsync(() -> {
            // Выполняем некоторые расчёты
        }).thenRun(() -> {                <---- Не имеет доступа к результату, т.к. Он принимает Runnable
            // Расчёты завершены
        });*/
    }
}
