package org.solve;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MergeIntervals {
    public static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        LinkedList<int[]> ans = new LinkedList<>();

        for (int[] interval : intervals) {
            if (ans.isEmpty() || ans.getLast()[1] < interval[0]) {
                ans.add(interval);
            } else {
                ans.getLast()[1] = Math.max(interval[1], ans.getLast()[1]);
            }
        }

        return ans.toArray(new int[ans.size()][]);
    }

    public static List<int[][]> getTestCases() {
        int[][] test1 = {{1,3},{2,6},{8,10},{15,18}};

        return Arrays.asList(test1);
    }

//    @SafeVarargs
//    @SuppressWarnings("varargs")
//    public static <T> T a(@NotNull T... b) {
//        System.out.println(b.getClass().getName());
//        return b[0];
//    }
}
