package ua.edu.ukma.generators.keys.integer;

import ua.edu.ukma.generators.AbstractPrecomputedGenerator;

import java.util.Random;

public class HotspotIntegerKeyGenerator extends AbstractPrecomputedGenerator<Integer> {

    public HotspotIntegerKeyGenerator(int numberOfKeys, int range, double hotSpotProbability, long seed) {
        super(computeKeys(numberOfKeys, range, hotSpotProbability, seed));
    }

    private static Integer[] computeKeys(int numberOfKeys, int range, double hotSpotProbability, long seed) {
        Random random = new Random(seed);
        Integer[] keys = new Integer[numberOfKeys];
        int hotSpotRange = Math.max(range / 100, 1);
        range = Math.max(range, hotSpotRange + 1);
        for (int i = 0; i < numberOfKeys; i++) {
            if (random.nextDouble() < hotSpotProbability) {
                keys[i] = random.nextInt(hotSpotRange);
            } else {
                keys[i] = random.nextInt(hotSpotRange, range);
            }
        }
        return keys;
    }
}
