package ua.edu.ukma.generators.keys.integer;

import ua.edu.ukma.generators.Generator;

public class IntegerKeyGeneratorsFactory {

    private IntegerKeyGeneratorsFactory() {}

    public static Generator<Integer> createIntegerKeyGenerator(ua.edu.ukma.generators.keys.integer.IntegerKeyDistribution distribution, int numberOfKeys, int range, long seed) {
        return switch (distribution) {
            case UNIFORM -> new UniformIntegerKeyGenerator(numberOfKeys, range, seed);
            case SEQUENTIAL -> new SequentialIntegerKeyGenerator(numberOfKeys, range);
            case HOTSPOT -> new HotspotIntegerKeyGenerator(numberOfKeys, range, 0.9, seed);
            case ZIPF -> new ZipfIntegerKeyGenerator(numberOfKeys, range, 1, seed);
        };
    }
}
