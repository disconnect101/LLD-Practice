package org.solve;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        int [][] arr = {{1,3},{2,6},{8,10},{15,18}};
        int [][] ans = MergeIntervals.merge(MergeIntervals.getTestCases().get(0));

        for (int[] interval : ans) {
            System.out.println(interval[0] + ", " + interval[1]);
        }

    }
}