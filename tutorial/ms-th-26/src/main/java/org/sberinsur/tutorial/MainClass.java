package org.sberinsur.tutorial;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainClass {
    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(new Integer[] {1, 2, 3, 4, 5, 6, 7});
        Iterator<Integer> iter1 = list.iterator();
        list.add(12);
        list.add(123);
        list.remove(2);
        list.remove(5);
        System.out.println("Iter1");
        while (iter1.hasNext()) {
            System.out.println(iter1.next());
        }
        System.out.println("*********************************************");
        System.out.println("Iter2");
        Iterator<Integer> iter2 = list.iterator();
        while (iter2.hasNext()) {
            System.out.println(iter2.next());
        }
    }
}
