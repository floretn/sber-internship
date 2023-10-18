package ru.sberinsur.tutorialProject;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {
    public String call(){
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            System.out.println("sout from MyCallable thread: " +i);
            sum+=i;
        }
        return Thread.currentThread() + " " + " умею возвращать результат вычислений " + sum;
    }
}
