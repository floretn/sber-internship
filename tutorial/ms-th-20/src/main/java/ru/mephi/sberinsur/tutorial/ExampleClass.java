package ru.mephi.sberinsur.tutorial;

import java.util.*;

public class ExampleClass {

    private enum Apples {
        GOLDEN,
        GRANNY_SMITH,
        GALA
    }

    private static class Person {
        String name;
        int salary;

        public Person(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return salary == person.salary && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, salary);
        }
    }

    public static void main(String[] args) {
        System.out.println("*********** SORTED MAP ***********");
        SortedMap<Integer, String> map = new TreeMap<>();
        map.put(5, "Five");
        map.put(1, "One");
        map.put(3, "Three");
        map.put(2, "Two");
        map.put(4, "Four");
        System.out.println(map);
        System.out.println(map.headMap(3));
        System.out.println(map.tailMap(3));
        SortedMap<Integer, String> map1 = new TreeMap<>((x, y) -> -Integer.compare(x, y));
        map1.putAll(map);
        System.out.println(map1);
        System.out.println(map1.firstKey());
        System.out.println(map1.subMap(4, 2));
        System.out.println();

        System.out.println("*********** NAVIGABLE MAP ***********");
        NavigableMap<Integer, String> map2 = new TreeMap<>(map);
        //map2.remove(2);
        System.out.println(map2.floorEntry(2));
        System.out.println(map2.higherKey(2));
        map2.put(2, "Two");
        System.out.println(map2.ceilingEntry(2));
        System.out.println(map2.pollFirstEntry());
        System.out.println(map2);
        System.out.println(map2.descendingMap());
        System.out.println();

        System.out.println("*********** ENUM MAP ***********");
        EnumMap<Apples, Integer> enumMap = new EnumMap<>(Apples.class);
        enumMap.put(Apples.GALA, 20);
        enumMap.put(Apples.GOLDEN, 30);
        enumMap.put(Apples.GRANNY_SMITH, 0);
        System.out.println(enumMap);
        System.out.println();

        System.out.println("*********** IDENTITY MAP ***********");

        IdentityHashMap<String, Integer> identityHashMap = new IdentityHashMap<>();
        String two = "Two";
        identityHashMap.put(new String(two), 2);
        identityHashMap.put(two, 22);
        System.out.println(identityHashMap);

        IdentityHashMap<Person, Integer> persons = new IdentityHashMap<>();
        persons.put(new Person("Name", 100), 20);
        Person pers = new Person("Name", 100);
        persons.put(pers, 10);
        System.out.println(persons);

        System.out.println(persons.get(new Person("Name", 100)));
        System.out.println(persons.get(pers));

        System.out.println("*********** WEAK MAP ***********");
        WeakHashMap<Integer, String> weakHashMap = new WeakHashMap<>();
        Integer i = 0;
        weakHashMap.put(1, "One");
        weakHashMap.put(i, "Zero");
        System.out.println(weakHashMap);
        List<Person> personList = new ArrayList<>();
        for (int j = 0; j < 10000; j++) {
            personList.add(new Person("name", j));
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(weakHashMap);
    }
}
