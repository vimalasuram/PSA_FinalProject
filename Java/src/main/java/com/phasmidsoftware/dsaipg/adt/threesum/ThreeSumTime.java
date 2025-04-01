package com.phasmidsoftware.dsaipg.adt.threesum;
import java.util.Arrays;
import java.util.Random;
import com.phasmidsoftware.dsaipg.util.Stopwatch;

public class ThreeSumTime {

    public static void main(String[] args) {
        int initialN = 200; // Start lower for cubic to work
        int iterations = 6;

        System.out.println("N, Cubic(ms), Quadratic(ms), Quadrithmic(ms)");

        for (int iter = 0; iter < iterations; iter++) {
            int N = initialN * (int) Math.pow(2, iter);
            int maxValue = 1000;
            int[] input = generateRandomArray(N, maxValue);

            long timeCubic = timeCubic(Arrays.copyOf(input, input.length));  // Ensure independent copies
            long timeQuadratic = timeQuadratic(Arrays.copyOf(input, input.length));
            long timeQuadrithmic = timeQuadrithmic(Arrays.copyOf(input, input.length));

            System.out.printf("array size:%d,Cubic(ms):%d,Quadratic(ms):%d,Quadrithmic(ms):%d%n", N, timeCubic, timeQuadratic, timeQuadrithmic);

        }
    }

    private static int[] generateRandomArray(int N, int maxValue) {
        Random rand = new Random();
        return rand.ints(N, -maxValue, maxValue).toArray();
    }

    private static long timeCubic(int[] input) {
        long elapsedMs;
        try (Stopwatch sw = new Stopwatch()) {
            new ThreeSumCubic(input).getTriples();
            elapsedMs = sw.lap();  // Ensure this correctly stops timing
        }
        return elapsedMs;
    }

    private static long timeQuadratic(int[] input) {
        long elapsedMs;
        try (Stopwatch sw = new Stopwatch()) {
            new ThreeSumQuadratic(input).getTriples();
            elapsedMs = sw.lap();
        }
        return elapsedMs;
    }

    private static long timeQuadrithmic(int[] input) {
        Arrays.sort(input);  // Sorting here ensures fair comparison for this method
        long elapsedMs;
        try (Stopwatch sw = new Stopwatch()) {
            new ThreeSumQuadrithmic(input).getTriples();
            elapsedMs = sw.lap();
        }
        return elapsedMs;
    }
}
