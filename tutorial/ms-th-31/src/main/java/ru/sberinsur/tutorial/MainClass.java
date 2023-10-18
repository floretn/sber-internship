package ru.sberinsur.tutorial;

import java.util.Arrays;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        List<String> people = Arrays.asList("Tom","Bob", "Sam", "Kate", "Tim");

        System.out.println("Последовательный поток");
        people.stream().filter(p->p.length()==3).forEach(System.out::println);

        System.out.println("\nПараллельный поток");
        people.parallelStream().filter(p->p.length()==3).forEach(System.out::println);
    }
}
