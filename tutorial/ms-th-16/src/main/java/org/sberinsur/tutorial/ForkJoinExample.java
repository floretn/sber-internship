package org.sberinsur.tutorial;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinExample {

    private static class Fibonacci extends RecursiveTask<Integer> {

        private final int number;

        public Fibonacci(int number) {
            this.number = number;
        }

        @Override
        protected Integer compute() {
            if (number <= 1) {
                return number;
            } else {
                Fibonacci fibonacciMinus1 = new Fibonacci(number - 1);
                Fibonacci fibonacciMinus2 = new Fibonacci(number - 2);
                fibonacciMinus1.fork();
                return fibonacciMinus2.compute() + fibonacciMinus1.join();
            }
        }
    }

    public static void main(String[] args) {

//        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
//        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfProcessors);

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        ForkJoinTask<Integer> result = forkJoinPool.submit(new Fibonacci(30));

        System.out.println("The result is: " + result.join());
    }
}
