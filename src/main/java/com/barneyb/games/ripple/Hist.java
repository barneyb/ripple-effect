package com.barneyb.games.ripple;

import java.util.Arrays;
import java.util.Collection;

public class Hist {

    private int[] hist = new int[0];

    public int get(int v) {
        return v < hist.length ? hist[v] : 0;
    }

    public void add(int v) {
        if (v >= hist.length) {
            hist = Arrays.copyOf(hist, Math.max(v + 1, hist.length * 2));
        }
        hist[v] += 1;
    }

    public void addAll(Collection<Integer> vs) {
        vs.forEach(this::add);
    }

    public int[] withCount(int count) {
        var n = 0;
        var result = new int[]{};
        for (int i = 0; i < hist.length; i++) {
            if (i > 0 && hist[i] == count) {
                if (n == result.length) {
                    result = Arrays.copyOf(result, n * 2 + 1);
                }
                result[n++] = i;
            }
        }
        return n < result.length
                ? Arrays.copyOf(result, n)
                : result;
    }

    @Override
    public String toString() {
        return "Hist{" +
               "hist=" + Arrays.toString(hist) +
               '}';
    }

}
