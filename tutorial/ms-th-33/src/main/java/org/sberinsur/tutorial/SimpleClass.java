package org.sberinsur.tutorial;

public class SimpleClass {
    int a;

    public SimpleClass(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "SimpleClass{" +
                "a=" + a +
                '}';
    }
}
