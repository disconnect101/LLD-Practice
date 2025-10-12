package org.solve.priorityqueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Test test = new Test();
        int[] arr = {90, 48, 56, 1, 91, 1000, -45};
        test.fill(arr);

        while (true) {
            A a = test.get();
            if (a == null) break;
            System.out.println(a.getA() + " " + a.getB());
        }
    }
}
