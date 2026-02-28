package ua.edu.ukma.generators.integer;

import ua.edu.ukma.generators.KeyGenerator;

public class IntegerKeyGeneratorsFactory {

    private IntegerKeyGeneratorsFactory() {}

    public static KeyGenerator<Integer> createIntegerKeyGenerator(IntegerKeyDistribution distribution, int numberOfKeys, int range, long seed) {
        return switch (distribution) {
            case UNIFORM -> new UniformIntegerKeyGenerator(numberOfKeys, range, seed);
            case SEQUENTIAL -> new SequentialIntegerKeyGenerator(numberOfKeys, range);
            case HOTSPOT -> new HotspotIntegerKeyGenerator(numberOfKeys, range, 0.9, seed);
            case ZIPF -> new ZipfIntegerKeyGenerator(numberOfKeys, range, 1, seed);
        };
    }
}
