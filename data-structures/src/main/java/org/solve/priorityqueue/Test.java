package org.solve.priorityqueue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Test {
    PriorityQueue<A> pq;

    public Test() {
        pq = new PriorityQueue<>(new Comparator<A>() {
            @Override
            public int compare(A a, A b) {
                return a.getA() - b.getA();
            }
        });
    }

    public void fill(int[] arr) {
        for (int i : arr) {
            pq.add(new A(i, i+1));
        }
    }

    public A get() {
        return pq.poll();
    }
}

class A {
    int a;
    int b;

    public A(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
