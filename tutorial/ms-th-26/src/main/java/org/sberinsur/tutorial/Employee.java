package org.sberinsur.tutorial;

import java.util.Arrays;

public class Employee {
    String name;
    boolean[] time = new boolean[24];

    Employee(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", time=" + Arrays.toString(time) +
                '}';
    }
}
