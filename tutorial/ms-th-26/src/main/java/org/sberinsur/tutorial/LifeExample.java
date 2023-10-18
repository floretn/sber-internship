package org.sberinsur.tutorial;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class LifeExample {

    private static final Employee[] employees = new Employee[] {new Employee("Ivan"),
            new Employee("Ilya"), new Employee("Edward"), new Employee("Nikita"),
            new Employee("Someone else")};

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        CopyOnWriteArrayList<Employee> nowOnWork = new CopyOnWriteArrayList<>(employees);
        for (int i = 0; i <= 23; i++) {
            int k = random.nextInt(5);
            if (nowOnWork.contains(employees[k])) {
                nowOnWork.remove(employees[k]);
                System.out.println("Remove on " + i + " iteration: " + employees[k]);
            } else {
                nowOnWork.add(employees[k]);
                System.out.println("Add on " + i + " iteration: " + employees[k]);
            }
            int j = i;
            new Thread(() -> {
                Iterator<Employee> iter = nowOnWork.iterator();
                while (iter.hasNext()) {
                    iter.next().time[j] = true;
                }
            }).start();
            Thread.sleep(1000);
            System.out.println("********************************************************************");
        }
        for (Employee e : employees) {
            System.out.println(e);
        }
    }
}
