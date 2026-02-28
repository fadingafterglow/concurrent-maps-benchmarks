package ua.edu.ukma.generators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ZipfIntegerKeyGenerator extends AbstractPrecomputedKeyGenerator<Integer> {

    public ZipfIntegerKeyGenerator(int numberOfKeys, int range, double s, long seed) {
        super(computeKeys(numberOfKeys, range, s, seed));
    }

    private static Integer[] computeKeys(int numberOfKeys, int range, double s, long seed) {
        Random random = new Random(seed);
        Integer[] keys = new Integer[numberOfKeys];
        double[] cdf = cdf(range, s);
        for (int i = 0; i < numberOfKeys; i++) {
            double p = random.nextDouble();
            int idx = Arrays.binarySearch(cdf, p);
            if (idx >= 0)
                keys[i] = idx;
            else
                keys[i] = Math.min(-idx - 1, range - 1);
        }
        return keys;
    }

    private static double[] cdf(int range, double s) {
        double[] cdf = new double[range];
        double sum = 0;
        for (int i = 1; i <= range; i++) {
            sum += 1 / Math.pow(i, s);
            cdf[i - 1] = sum;
        }
        for (int i = 0; i < range; i++) {
            cdf[i] /= sum;
        }
        return cdf;
    }
}
