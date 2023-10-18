package org.sberinsur.tutorial;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class InterviewTask {

    private static class CheckFibonacci extends RecursiveTask<Boolean> {

        private final long numberToCheck;

        private final long previousN;
        private final long currentN;

        public CheckFibonacci(long numberToCheck) {
            this(1, 1, numberToCheck);
        }

        private CheckFibonacci(long previousN, long currentN, long numberToCheck) {
            this.previousN = previousN;
            this.currentN = currentN;
            this.numberToCheck = numberToCheck;
        }

        @Override
        protected Boolean compute() {
//            System.out.println(Thread.currentThread() + " " + currentN);
            if (currentN > numberToCheck) {
                return false;
            }
            CheckFibonacci checkFibonacciNext = new CheckFibonacci(currentN, currentN + previousN, numberToCheck);
            checkFibonacciNext.fork();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            if (currentN < numberToCheck) {
                return checkFibonacciNext.join();
            }
            return true;
        }
    }

    private static boolean checkFib(long number) {
        long previous = 1;
        long current = 1;
        while (current < number) {
            long i = previous;
            previous = current;
            current = i + previous;
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        return current == number;
    }

    public static void main(String[] args) {
        //4052739537881L;
        long number = 4052739537881L;

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long timeStart = System.nanoTime();
        ForkJoinTask<Boolean> result = forkJoinPool.submit(new InterviewTask.CheckFibonacci(number));
        System.out.println("Result ForkJoin = " + result.join());
        System.out.println("For time = " + (System.nanoTime() - timeStart));

        System.out.println("********************************************************************************************");

        timeStart = System.nanoTime();
        System.out.println("Result = " + checkFib(number));
        System.out.println("For time = " + (System.nanoTime() - timeStart));
    }
}
