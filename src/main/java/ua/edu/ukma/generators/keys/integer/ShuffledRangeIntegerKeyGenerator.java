package ua.edu.ukma.generators.keys.integer;

import ua.edu.ukma.generators.AbstractPrecomputedGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ShuffledRangeIntegerKeyGenerator extends AbstractPrecomputedGenerator<Integer> {

    public ShuffledRangeIntegerKeyGenerator(int numberOfKeys, int offset, long seed) {
        super(computeKeys(numberOfKeys, offset, seed));
    }

    private static Integer[] computeKeys(int numberOfKeys, int offset, long seed) {
        Integer[] keys = new Integer[numberOfKeys];
        for (int i = 0; i < numberOfKeys; i++) {
            keys[i] = offset + i;
        }
        Collections.shuffle(Arrays.asList(keys), new Random(seed));
        return keys;
    }
}
