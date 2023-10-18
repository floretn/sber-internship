package org.sberinsur;

import java.util.IntSummaryStatistics;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class MainReduceDemo {
    public static void main(String[] args) {
        /**
         *  Optional<T> reduce(BinaryOperator<T> accumulator) - это специальный тип (подинтерфейс) BiFunction,
         *  который принимает два операнда одного типа 'T' и возвращает результат того же типа T.
         *  Метод reduce() итеративно применяет функцию аккумулятора к текущему входному элементу.
         */
        int i = IntStream.range(1, 6)
                .reduce((a, b) -> a * b)
                .orElse(-1);
        System.out.println(i);

        /**
         *  T reduce(T identity, BinaryOperator<T> accumulator)
         *  Идентификатор является результатом сокращения по умолчанию,
         *  если в потоке нет элементов. Вот почему эта версия метода reduce не возвращает Optional,
         *  потому что она, по крайней мере, возвращает элемент identity.
         */
        int i1 = IntStream.empty()
                .reduce(1, (a, b) -> a * b);
        System.out.println(i1);

        /**
         * <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)
         * Этот метод представляет собой комбинацию и операции.map()reduce()
         * Значение identity должно быть идентификатором для функции combiner:
         * Аккумулятор BiFunction (2-й параметр) предназначен для сопоставления элемента потока типа T с U, и при этом он делает накопление.
         * Комбайнер BinaryOperator (3-й параметр) специально необходим в параллельных потоках для объединения различных результатов разделения вместе в конце
         *
         */
        int i2 = Stream.of("2", "3", "4", "5")
                .parallel()
                .reduce(0, (integer, s) -> Integer.sum(integer, Integer.parseInt(s)),
                        (integer, integer2) -> Integer.sum(integer, integer2));
        System.out.println(i2);

        /**
         * Optional<T> min(Comparator<? super T> comparator)
         * Возвращает минимальный элемент этого потока в соответствии с предоставленным компаратором.
         * В этом примере возвращается строка min в соответствии с лексическим порядком (словарным порядком):
         */
        String s = Stream.of("banana", "pie", "apple")
                .min(String::compareTo) //dictionary order
                .orElse("None");
        System.out.println(s);

        /**
         * Optional<T> max(Comparator<? super T> comparator)
         * Возвращает максимальный элемент этого потока в соответствии с предоставленным компаратором.
         * Подобно методу min(), это частный случай reduce():
         */
        String s1 = Stream.of("banana", "pie", "apple")
                .max(String::compareTo) //dictionary order
                .orElse("None");
        System.out.println(s1);

        /**
         * Методы Sum()
         * Эти методы возвращают сумму элементов в потоке.
         */
        double sum = DoubleStream.of(1.1, 1.5, 2.5, 5.4).sum();
        System.out.println(sum);

        /**
         * методы average()
         * Эти методы возвращают среднее арифметическое элементов потока.
         */
        double v = LongStream.range(1, 10).average().orElse(-1);
        System.out.println(v);

        /**
         * long count()
         * Этот метод возвращает размер (количество элементов) потока.
         * Он также определен в IntStream, LongStream и DoubleStream.
         */
        long c = Stream.of("banana", "pie", "apple").count();
        System.out.println(c);

        /**
         * summaryStatistics() methods
         * Эти методы возвращают объект состояния с такими сведениями, как count, min, max, sum и average.
         */
        IntSummaryStatistics s2 = IntStream.rangeClosed(1, 10)
                .summaryStatistics();
        System.out.println(s2);

        /**
         * Термины, которые полезно запомнить
         * Аккумулятор
         * Аккумулятор — это двоичная операция, первый аргумент которой — это значение,
         * возвращенное при последнем выполнении той же операции, а второй аргумент — текущий входной элемент.
         *
         * Комбинатор
         * Комбинатор — это двоичная функция,
         * которая принимает два независимых результата из двух параллельных потоков и возвращает комбинированный результат.
         *
         * Операция складывания
         * Операция складывания последовательно использует аккумулятор на каждом входном элементе
         * и объединяет их в один конечный результат. Все методы reduce() являются операциями складывания.
         */


    }
}
