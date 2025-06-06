package com.phasmidsoftware.dsaipg.sort.classic;

import com.phasmidsoftware.dsaipg.sort.Helper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ClassicSortTest {

    static class MyClass implements Classify<MyClass> {
        public MyClass(int value) {
            this.value = value;
        }

        public int classify() {
            return value / 1000;
        }

        private final int value;

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    @Test
    public void mutatingSort() throws IOException {
        ClassicSort<MyClass> sorter = new ClassicSort<>();
        Helper<MyClass> helper = sorter.getHelper();
        int n = 100;
        helper.init(n);
        MyClass[] xs = helper.random(MyClass.class, (random -> new MyClass(random.nextInt(100000))));
        sorter.mutatingSort(xs);
        // Check sorted
        for (int i = 1; i < n; i++) assertTrue(xs[i - 1].classify() <= xs[i].classify());
    }
}