package org.sberinsur.tutorial;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainClass {
    public static void main(String[] args) {
        List<SimpleClass> list = Arrays.asList(new SimpleClass(1), new SimpleClass(5));

        /*
        * Stateful - distinct(), sorted(), limit(), skip() - операции,
        * каждый шаг которых зависит от внутреннего состояния предыдущего шага.
        * Stateful behavior parameter - некоторое поведение, использующее и меняющее состояние объектов
        * и принимаемое на вход методов.
        *
        * Stateless - любые другие операции (на пример - findFirst).
        */
        List<SimpleClass> rez = list.stream().limit(1).collect(Collectors.toList());
        System.out.println(rez);

        rez = list.stream().map(x -> {
            if (x.a < 5) {
                x.a = x.a * 7;
                return x;
            }
            x.a = x.a - 2;
            return x;
        }).collect(Collectors.toList());
        System.out.println(rez);

        //Stateless
        System.out.println(list.stream().findFirst().get());

        /*
        * Операции короткого замыкания - картинка в ресурсах.
        * Операции, которые могут уменьшить размер потока.
        * И если размер был уменьшен до 0, то остальные операции выполняться не будут.
        * Естественно, это сработает только при вызове детерминальной операции.
        */

        /*
        * Сортированные операции - sorted, of и тп, которые задают порядок обработки объектов.
        */
    }
}
