package org.sberinsur.tutorial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class MainClass {
    public static void main(String[] args) {

        /*
         * Проверяет условие по двум переменным.
         */
        BiPredicate<Integer, String> predicate = (x, s) -> s.length() > x;
        System.out.println(predicate.test(10, "1234567890"));
        System.out.println(predicate.negate().test(10, "1234567890"));
        System.out.println(predicate.and((x, s) -> s.equals(x + "")).test(10, "10"));
        System.out.println(predicate.or((x, s) -> s.equals(x + "")).test(10, "10"));

        System.out.println("******************************************************************");

        /*
         * Принимает 2 аргумента, а возвращает третий тип.
         */
        BiFunction<Double, Integer, String> function = (d, x) -> d + x + "";
        System.out.println(function.apply(12.0, 12));
        System.out.println(function.andThen(s -> "result: " + s).apply(12.0, 12));

        System.out.println("******************************************************************");

        /*
         * Принимает 2 аргумента, и что-то с этим делает.
         * Аналогично БиФункшен имеет метод эндЗен.
         */
        BiConsumer<List<Integer>, Integer> consumer = (a, x) -> {
            for (int i = 0; i < a.size(); i++) {
                a.set(i, a.get(i) + x);
            }
        };
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        consumer.accept(ints, 12);
        System.out.println(ints);

        System.out.println("******************************************************************");
        
    }
}
