package ua.edu.ukma.generators;

public class SequentialIntegerKeyGenerator extends AbstractPrecomputedKeyGenerator<Integer> {

    public SequentialIntegerKeyGenerator(int numberOfKeys, int range) {
        super(computeKeys(numberOfKeys, range));
    }

    private static Integer[] computeKeys(int numberOfKeys, int range) {
        Integer[] keys = new Integer[numberOfKeys];
        for (int i = 0; i < numberOfKeys; i++) {
            keys[i] = i % range;
        }
        return keys;
    }
}
