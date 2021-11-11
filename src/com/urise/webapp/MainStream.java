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
        return Arrays.stream(values).sorted().distinct().reduce(0, (a, b) -> (int) Math.pow(10, (int) (Math.log10(b) + 1)) * a + b);

    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream()
                .filter(integers.stream().mapToInt(Integer::intValue).sum() % 2 != 0 ? a -> a % 2 == 0 : a -> a % 2 != 0)
                .collect(Collectors.toList());
    }
}
