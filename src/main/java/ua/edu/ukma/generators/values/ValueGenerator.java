package ua.edu.ukma.generators.values;

import ua.edu.ukma.generators.AbstractPrecomputedGenerator;

public class ValueGenerator extends AbstractPrecomputedGenerator<Object> {

    public ValueGenerator(int numberOfValues) {
        super(computeValues(numberOfValues));
    }

    private static Object[] computeValues(int numberOfValues) {
        Object[] values = new Object[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            values[i] = new Object();
        }
        return values;
    }
}
