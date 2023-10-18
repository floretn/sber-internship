import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

public class ConcurrentMapDemoMain {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < 10; i++) {
            hashMap.put(i, i);
        }

        System.out.println("=============== HASHMAP ===========================");
        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>(hashMap);
        //Вставить элемент, если ключ отсутствует.
        concurrentHashMap.putIfAbsent(11,11);
        System.out.println(concurrentHashMap);
        //Попробовать удалить элемент
        System.out.println(concurrentHashMap.remove(12,12));
        //Заменить старое значение на новое по ключу, если старое соответствует заданному
        concurrentHashMap.replace(11,11,110);
        System.out.println(concurrentHashMap);
        //Заменяет значение, если к ключу привязано значение
        concurrentHashMap.replace(1,10);
        System.out.println(concurrentHashMap);

        ConcurrentSkipListMap<Integer, Integer> concurrentSkipListMap = new ConcurrentSkipListMap<Integer, Integer>(hashMap);
        System.out.println("=============== SkipListMap ===========================");
        //Возвращает SkipListMap до указанного ключа
        System.out.println(concurrentSkipListMap.headMap(11, true));
        concurrentSkipListMap.putIfAbsent(12,12);
        System.out.println(concurrentSkipListMap.headMap(11,true));
        //Ближайшая па сверху к заданному ключу или сама пара
        System.out.println(concurrentSkipListMap.floorEntry(11));
        //Вовзращает SkipListMap после указанного ключа
        System.out.println(concurrentSkipListMap.tailMap(5));
        //Ближайший к заданному наибольший ключ
        System.out.println(concurrentSkipListMap.higherKey(11));
        //Первыый элемент в коллекции, есть аналогия с последним
        System.out.println(concurrentSkipListMap.pollFirstEntry());
        //Возвращает наименьший ключ, превышающий или равный заданному ключу, или если такого ключа нет.null
        System.out.println(concurrentSkipListMap.ceilingKey(11));


    }
}


