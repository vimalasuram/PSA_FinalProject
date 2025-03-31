/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreeSumQuadratic implements ThreeSum {

    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }


    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++) triples.addAll(getTriples(i));
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }


     List<Triple> getTriples(int j) {
        List<Triple> triples = new ArrayList<>();
        int l = 0;
        int r = length - 1;

        while (l < j && j < r) {
            int res = a[l] + a[j] + a[r];

            if (res == 0) {
                triples.add(new Triple(a[l], a[j], a[r]));
                l++;
                r--;
            } else if (res < 0) {
                l++;
            } else {
                r--;
            }
        }

        return triples;
    }

    private final int[] a;
    private final int length;
}