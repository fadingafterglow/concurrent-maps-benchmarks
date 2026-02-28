package ua.edu.ukma.generators.keys.integer;

import ua.edu.ukma.generators.AbstractPrecomputedGenerator;

import java.util.Random;

public class UniformIntegerKeyGenerator extends AbstractPrecomputedGenerator<Integer> {

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
