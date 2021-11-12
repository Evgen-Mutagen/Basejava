package com.urise.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        int[] val = new int[]{1, 2, 5, 3, 5, 3, 2, 8, 1};
        System.out.println(minValue(val));
        System.out.println(oddOrEven(Arrays.asList(4, 5, 4, 3, 2, 6, 7, 6)));
    }

    public static int minValue(int[] values) {
        return Arrays.stream(values).
                distinct().
                sorted().
                reduce(0, (a, b) -> 10 * a + b);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int modSum = integers.stream().
                mapToInt(Integer::intValue).
                sum() % 2;

        return integers
                .stream()
                .filter(a -> a % 2 != modSum)
                .collect(Collectors.toList());
    }
}
