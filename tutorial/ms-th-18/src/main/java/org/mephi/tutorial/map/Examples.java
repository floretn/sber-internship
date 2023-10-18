package org.mephi.tutorial.map;

import java.util.*;

public class Examples {
    public static void main(String[] args) {
        System.out.println("**************************************************************************");
        /*
        * Ключи должны переопределять методы equals() и hashCode() и быть immutable
        * Порядок не важен
        * Доступ к элементам за О(1) или О(n) в худшем случае
        */
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Moscow", 2000000);
        hashMap.put("Archangelsk", 400000);
        hashMap.put("Kargopol", 100);
        System.out.println(hashMap);

        hashMap.put("Moscow", 2000002);
        hashMap.put("New", 123);
        System.out.println(hashMap);
        System.out.println("**************************************************************************");
        /*
         * Ключи должны имплементировать Comparable и быть immutable
         * Порядок важен
         * Доступ к элементам за О(log(n))
         */
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(12, "Tower 1");
        treeMap.put(29, "Tower 2");
        treeMap.put(41, "Tower 3");
        System.out.println(treeMap);
        System.out.println("Near Tower is " + returnNearTower(treeMap, 13));
        treeMap.remove(12);
        treeMap.put(52, "Tower 4");
        System.out.println(treeMap);
        System.out.println("Near Tower is " + returnNearTower(treeMap, 40));
        System.out.println("**************************************************************************");
        /*
        * Та же хеш-карта, только сохраняется порядок вставки элементов за счёт впаянного двусвязного списка внутрь
        */
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>(hashMap);
        linkedHashMap.put("Any Tower", 2000);
        hashMap.put("Any Tower", 2000);
        System.out.println(linkedHashMap);
        System.out.println(hashMap);
        System.out.println("**************************************************************************");
        /*
        * Бонусом полезные методы
        */
        Map<Integer, String> map = Collections.emptyMap();
        System.out.println(map);
        //map.put(1, "one");
        map = Collections.singletonMap(1, "one");
        System.out.println(map);
        map.put(2, "Two");
    }

    private static String returnNearTower(TreeMap<Integer, String> map, int key) {
        Map.Entry<Integer, String> lowEntry = map.lowerEntry(key);
        Map.Entry<Integer, String> highEntry = map.higherEntry(key);
        if (highEntry.getKey() - key < key - lowEntry.getKey()) {
            return highEntry.getValue();
        }
        return lowEntry.getValue();
    }
}
