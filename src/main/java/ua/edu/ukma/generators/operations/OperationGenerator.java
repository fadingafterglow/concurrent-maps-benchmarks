package ua.edu.ukma.generators.operations;

import ua.edu.ukma.generators.AbstractPrecomputedGenerator;

import java.util.Random;

public class OperationGenerator extends AbstractPrecomputedGenerator<OperationType> {

    public OperationGenerator(int numberOfOperations, double getProbability, double putProbability, long seed) {
        super(computeOperations(numberOfOperations, getProbability, putProbability, seed));
    }

    private static OperationType[] computeOperations(int numberOfOperations, double getProbability, double putProbability, long seed) {
        Random random = new Random(seed);
        OperationType[] operations = new OperationType[numberOfOperations];
        for (int i = 0; i < numberOfOperations; i++) {
            double p = random.nextDouble();
            if (p < getProbability) {
                operations[i] = OperationType.GET;
            } else if (p < getProbability + putProbability) {
                operations[i] = OperationType.PUT;
            } else {
                operations[i] = OperationType.DELETE;
            }
        }
        return operations;
    }
}
