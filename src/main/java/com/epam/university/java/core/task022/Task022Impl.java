package com.epam.university.java.core.task022;

import java.util.Collection;
import java.util.Collections;

public class Task022Impl implements Task022 {
    @Override
    public int maxSum(Collection<Integer> numbers) {
        int min = Collections.min(numbers);
        return numbers.stream().mapToInt(n -> n).sum() - min;
    }

    @Override
    public int minSum(Collection<Integer> numbers) {
        int max = Collections.max(numbers);
        return numbers.stream().mapToInt(n -> n).sum() - max;
    }
}
