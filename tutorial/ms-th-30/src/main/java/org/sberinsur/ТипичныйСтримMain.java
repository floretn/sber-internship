package org.sberinsur;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ТипичныйСтримMain {
    public static void main(String[] args) {
        int sum = IntStream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                //Берем лишь некоторые элементы
                .filter(x -> x % 2 == 0)
                //преобразуем каждый элемент
                .map(x -> x + 1)
                //создаем единый объект, в данном случае сумму чисел.
                .reduce(0, Integer::sum);
        System.out.println(sum);

        List<Integer> integerList =  Stream.iterate(1, n -> n + 1)
                //Ограничиваемся десятью элементами
                .limit(10)
                //Берем лишь некоторые элементы
                .filter(x -> x % 2 == 0)
                //преобразуем каждый элемент
                .map(x -> x + 1)
                //создаем List из стрима
                .collect(Collectors.toList());

        System.out.println(integerList);
    }
}
