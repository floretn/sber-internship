package org.sberinsur;

import java.util.Scanner;
import java.util.function.*;

public class FuncDemoMainApplication {
    public static void main(String[] args) {
        /**  Predicate<T t> - проверяет какое-то условие
         * boolean test(T t)    */
        Predicate<Integer> isPositive = x -> x > 0;

        System.out.println(isPositive.test(5)); // true
        System.out.println(isPositive.test(-7)); // false

        /** BinaryOperator<T> принимает в качестве параметра два объекта типа T,
         *  выполняет над ними бинарную операцию и возвращает ее результат также в виде объекта типа T:
         *  T apply(T t1, T t2) */
        BinaryOperator<Integer> multiply = (x, y) -> x*y;

        System.out.println(multiply.apply(3, 5)); // 15
        System.out.println(multiply.apply(10, -2)); // -20

        /** UnaryOperator<T> принимает в качестве параметра объект типа T,
         *  выполняет над ними операции и возвращает результат операций в виде объекта типа T:
         * T apply(T t);        */
        UnaryOperator<Integer> square = x -> x*x;
        System.out.println(square.apply(5)); // 25

        /** Функциональный интерфейс Function<T,R> представляет функцию перехода от объекта типа T к объекту типа R:
         *  R apply(T t)        */
        Function<Integer, String> convert = x-> String.valueOf(x) + " долларов";
        System.out.println(convert.apply(5)); // 5 долларов

        /** Consumer<T> выполняет некоторое действие над объектом типа T, при этом ничего не возвращая:
         * void accept(T t)     */
        Consumer<String> greetings = x -> System.out.println("Приветствую, " + x + " !!!");
        greetings.accept("Коллеги");

        /** Supplier<T> не принимает никаких аргументов, но должен возвращать объект типа T:
         * T get()              */
        Supplier<User> userFactory = ()->{
            Scanner in = new Scanner(System.in);
            System.out.println("Введите имя: ");
            String name = in.nextLine();
            return new User(name);
        };

        User user1 = userFactory.get();
        User user2 = userFactory.get();

        System.out.println("Имя user1: " + user1.getName());
        System.out.println("Имя user2: " + user2.getName());
    }
}
    class User{
        private String name;
        String getName(){
            return name;
        }

        User(String n){
            this.name=n;
        }
}

