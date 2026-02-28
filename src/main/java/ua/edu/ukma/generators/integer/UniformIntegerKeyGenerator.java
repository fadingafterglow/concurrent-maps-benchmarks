package ua.edu.ukma.generators.integer;

import ua.edu.ukma.generators.AbstractPrecomputedKeyGenerator;

import java.util.Random;

public class UniformIntegerKeyGenerator extends AbstractPrecomputedKeyGenerator<Integer> {

    public UniformIntegerKeyGenerator(int numberOfKeys, int range, long seed) {
        super(computeKeys(numberOfKeys, range, seed));
    }

    private static Integer[] computeKeys(int numberOfKeys, int range, long seed) {
        Random random = new Random(seed);
        Integer[] keys = new Integer[numberOfKeys];
        for (int i = 0; i < numberOfKeys; i++) {
            keys[i] = random.nextInt(range);
        }
        return keys;
    }
}
