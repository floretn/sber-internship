package org.sberinsur;

import java.util.Arrays;
import java.util.List;

public class MainMutableReductionDemo {
    public static void main(String[] args) {

        /**
         * <R> R collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner)
         * Mutable Reduction версия, все объекты складываются в один изменяемый контейнер StringBuilder
         */
        List<String> list = Arrays.asList("Mike", "Nicki", "John");
        String s = list.stream().collect(StringBuilder::new,
                (sb, s1) -> sb.append(" ").append(s1),
                (sb1, sb2) -> sb1.append(sb2.toString())).toString();
        System.out.println(s + "Mutable Reduction");

        /**
         * Reduction версия,каждый следующий объект являтеся результатом
         * конкатенации предыдущих - менее эфективна по производительности,
         * так как каждый раз создается новый объект
         */
        List<String> list1 = Arrays.asList("Mike", "Nicki", "John");
        String sa = list1.stream().reduce("", (s1, s2) -> s1 + " " + s2);
        System.out.println(sa);

        /**
         * <R,A> R collect(Collector<? super T,A,R> collector)
         * T => тип базового потока,
         * A => контейнер с изменяемым аккумулятором, возвращенный поставщиком,
         * R => конечный тип, который возвращается из вызова finisher(), так же, как и из вызова Stream#collect().
         */
        List<String> list2 = Arrays.asList("Mike", "Nicki", "John");
        String s2 = list.stream().collect(StringBuilder::new,
                (sb, s1) -> sb.append(" ").append(s1),
                (sb1, sb2) -> sb1.append(sb2.toString())).toString();

        /**
         *  Что такое  Collector.characteristics():
         *  Набор характеристик, заданных реализацией коллектора,
         *  заключается в предоставлении следующей одной или нескольких подсказок оптимизации конвейеру потока обработки.
         */
    }
}
