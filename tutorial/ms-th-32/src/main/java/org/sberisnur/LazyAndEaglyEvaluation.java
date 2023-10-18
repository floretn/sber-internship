package org.sberisnur;

import java.time.LocalTime;
import java.util.stream.IntStream;

public class LazyAndEaglyEvaluation {
    public static void main (String[] args) {
        IntStream stream1 = IntStream.range(1, 5);
        stream1= stream1.peek(i -> log("начал", i))
                .filter(i -> { log("отфильтровал", i);
                    return i % 2 == 0;})
                .peek(i -> log("отфильтровал", i));
        log("Вызвал терминальный метод подсчета элементов");
        log("количество элементов - ", stream1.count());

        System.out.println("#######################");

        IntStream stream2 = IntStream.range(1, 5).parallel();
        stream2 = stream2.peek(i -> log("начал", i))
                .filter(i -> {log("отфильтровал", i);
                    return i % 2 == 0;})
                .peek(i -> log("отфильтровал", i));
        log("вызов терминального метода подсчета элементов");
        log("количество элементов - ", stream2.count());
    }

    public static void log (Object... objects) {
        String s = LocalTime.now().toString();
        for (Object object : objects) {
            s += " - " + object.toString();
        }
        System.out.println(s);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
