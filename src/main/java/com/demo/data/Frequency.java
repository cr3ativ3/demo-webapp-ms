package com.demo.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;

public class Frequency {

    public static final Frequency ZERO = new Frequency(0L);

    private AtomicLong value;

    public Frequency(long initialValue) {
        this.value = new AtomicLong(initialValue);
    }

    public long longValue() {
        return this.value.get();
    }

    @Override
    public String toString() {
        return String.valueOf(longValue());
    }

    public static <T> Map<T, Frequency> countToMap(List<T> items) {
        return items.stream().collect(groupingBy(Function.identity(), countingFrequency()));
    }

    private static <T> Collector<T, ?, Frequency> countingFrequency() {
        return reducing(Frequency.ZERO, e -> new Frequency(1L), Frequency::sum);
    }

    private static Frequency sum(Frequency lh, Frequency rh) {
        return new Frequency(lh.longValue() + rh.longValue());
    }
}
