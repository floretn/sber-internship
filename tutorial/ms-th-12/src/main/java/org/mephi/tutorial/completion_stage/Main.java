package org.mephi.tutorial.completion_stage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Main {
    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName());

        //Выполнение задач друг за другом в главном потоке
        CompletionStage<Integer> completionStage = new CompletableFuture<>();
        int a = 10;
        completionStage.thenApply(x -> x*x*x*x*x*x*x*x)
                .thenAccept(System.out::println)
                .thenRun(() -> System.out.println("Конец вычислений"));

        completionStage.toCompletableFuture().complete(a);

        //Можно использовать только один раз
        completionStage.toCompletableFuture().complete(a);

        System.out.println("******************************");

        //TODO-----------------------------------------------------------------------------------------------------

        //Выполнение задач друг за другом, но в отдельном потоке
        //completionStage = new CompletableFuture<>();
        completionStage.thenApplyAsync(x -> {
                    System.out.println(Thread.currentThread().getName());
                    return x*x*x*x*x*x*x*x;
                }).thenAccept((x) -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(x);})
                .thenRun(() -> System.out.println("Конец вычислений"))
                .thenRun(() -> System.out.println("******************************"));
        completionStage.toCompletableFuture().complete(a);

        //Можно посмотреть, что будет, если менять 28 строку
        System.out.println("Stroka");

        //TODO-----------------------------------------------------------------------------------------------------

        //Выполнение каждой задачи в отдельном потоке
        completionStage = new CompletableFuture<>();
        completionStage.thenApplyAsync(x -> {
                    System.out.println(Thread.currentThread().getName());
                    return x*x;
                }).thenAcceptAsync((x) -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(x);})
                .thenRun(() -> System.out.println("Конец вычислений"))
                .thenRun(() -> System.out.println("******************************"));
        completionStage.toCompletableFuture().complete(a);

        //TODO-----------------------------------------------------------------------------------------------------

        /*
         * По аналогии с тремя методами в самом верху, есть по 3 метода по типу метода ниже:
         * thenAcceptBoth(Async), runAfterBoth(Async)
         */
        completionStage = new CompletableFuture<>();
        CompletionStage<Integer> completionStage1 = new CompletableFuture<>();

        completionStage.thenApply(x -> x*x).thenCombine(completionStage1.thenApply((y) -> y*y),
                Integer::sum).thenAccept((x) -> System.out.println("result: " + x))
                .thenRun(() -> System.out.println("******************************"));

        completionStage.toCompletableFuture().complete(12);
        completionStage1.toCompletableFuture().complete(11);

        //TODO-----------------------------------------------------------------------------------------------------

        /*
         * По аналогии с тремя методами в самом верху, есть по 3 метода по типу метода ниже:
         * acceptEither(Async), runAfterEither(Async)
         */
        completionStage = new CompletableFuture<>();
        completionStage1 = new CompletableFuture<>();

        completionStage.thenApply(x -> x*x).applyToEither(completionStage1.thenApply((y) -> y*y),
                Math::sqrt).thenAccept((x) -> System.out.println("result: " + x))
                .thenRun(() -> System.out.println("******************************"));

        //Поиграться с перестановкой штук
        //completionStage1.toCompletableFuture().complete(11);
        completionStage.toCompletableFuture().complete(12);
        completionStage1.toCompletableFuture().complete(11);

        //TODO-----------------------------------------------------------------------------------------------------

        completionStage = new CompletableFuture<>();
        CompletionStage<Integer> completionStage2 = new CompletableFuture<>();

        completionStage.thenApply(x -> x*x)
                .thenCompose((x) -> completionStage2.thenApply((y) -> y+x)).thenAccept(System.out::println)
                .thenRun(() -> System.out.println("******************************"));

        completionStage.toCompletableFuture().complete(12);
        completionStage2.toCompletableFuture().complete(11);

        //TODO-----------------------------------------------------------------------------------------------------

        completionStage = new CompletableFuture<>();

        completionStage.thenApply(x -> 12/x).handle((x, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
                return 0;
            }
            return x;
        }).thenAccept(System.out::println)
                .thenRun(() -> System.out.println("******************************"));

        //Поиграться с 0 и 6
        completionStage.toCompletableFuture().complete(0);

        //TODO-----------------------------------------------------------------------------------------------------

        completionStage = new CompletableFuture<>();

        completionStage.thenApply(x -> 12/x).whenComplete((x, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                System.out.println(ex.getMessage() + "\nWhen Complete");
                return;
            }
            System.out.println(x);
        }).thenRun(() -> System.out.println("******************************"));

        //Поиграться с 0 и 6
        completionStage.toCompletableFuture().complete(0);

        //TODO-----------------------------------------------------------------------------------------------------

        CompletionStage<Integer> completionStage3 = new CompletableFuture<>();

        completionStage3.thenApply(x -> 12/x).exceptionally((ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
                return 0;
            }
            System.out.println("All is done!");
            return 1;
        }).thenRun(() -> System.out.println("******************************"));

        //Поиграться с 0 и 6
        completionStage3.toCompletableFuture().complete(0);
    }
}
