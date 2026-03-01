package ua.edu.ukma.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import ua.edu.ukma.generators.Generator;
import ua.edu.ukma.generators.keys.integer.IntegerKeyDistribution;
import ua.edu.ukma.generators.keys.integer.IntegerKeyGeneratorsFactory;
import ua.edu.ukma.generators.operations.OperationGenerator;
import ua.edu.ukma.generators.operations.OperationType;
import ua.edu.ukma.generators.values.ValueGenerator;
import ua.edu.ukma.maps.ConcurrentMapType;
import ua.edu.ukma.maps.ConcurrentMapsFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class IntegerKeysMixedOperationsBenchmark {

    private static final int PREGENERATION_SIZE = 1_000_000;
    private static final int DISTRIBUTION_ACCURATE_PREFILL_ATTEMPTS = 5;

    @Param
    private ConcurrentMapType mapType;

    @Param("0.5")
    private double prefillFactor;

    @Param({"100:0", "90:5", "70:20", "30:35", "10:45", "0:50"})
    private String operationMix; // e.g., "50:30" means 50% get, 30% put, 20% delete

    @Param
    private IntegerKeyDistribution keyDistribution;

    @Param({"100", "10000", "1000000"})
    private int keyRange;

    @Param("42")
    private long seed;

    private Random random;
    private Map<Integer, Object> map;

    @Setup(Level.Trial)
    public void setUpTrial() {
        random = new Random(seed);
    }

    @Setup(Level.Iteration)
    public void setUpIteration(BenchmarkParams params) {
        map = ConcurrentMapsFactory.createConcurrentMap(mapType, params.getThreads());
        prefillMap(map);
    }

    private void prefillMap(Map<Integer, Object> map) {
        int prefillSize = (int)(prefillFactor * keyRange);
        int attempts = DISTRIBUTION_ACCURATE_PREFILL_ATTEMPTS;
        int insertedCount = 0;
        while (attempts > 0) {
            Generator<Integer> prefillKeyGenerator = IntegerKeyGeneratorsFactory
                    .createIntegerKeyGenerator(keyDistribution, prefillSize, keyRange, random.nextLong());
            for (int i = 0; i < prefillSize; i++) {
                boolean inserted = map.put(prefillKeyGenerator.next(), new Object()) == null;
                if (inserted && ++insertedCount >= prefillSize) return;
            }
            attempts--;
        }
        for (int i = 0; i < keyRange; i++) {
            boolean inserted = map.put(i, new Object()) == null;
            if (inserted && ++insertedCount >= prefillSize) return;
        }
    }

    @TearDown(Level.Iteration)
    public void tearDownIteration() {
        map = null;
    }

    @TearDown(Level.Trial)
    public void tearDownTrial() {
        random = null;
    }

    @State(Scope.Thread)
    public static class WorkerState {
        private Generator<Integer> keyGenerator;
        private Generator<OperationType> operationGenerator;
        private Generator<Object> valueGenerator;

        @Setup(Level.Iteration)
        public void setupIteration(IntegerKeysMixedOperationsBenchmark benchmark) {
            long seed;
            synchronized (benchmark) {
                seed = benchmark.random.nextLong();
            }
            keyGenerator = IntegerKeyGeneratorsFactory
                    .createIntegerKeyGenerator(benchmark.keyDistribution, PREGENERATION_SIZE, benchmark.keyRange, seed);
            double[] operationProbabilities = Utils.parseOperationMix(benchmark.operationMix);
            operationGenerator = new OperationGenerator(PREGENERATION_SIZE, operationProbabilities[0], operationProbabilities[1], seed);
            valueGenerator = new ValueGenerator(10);
        }
    }


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object benchmark(WorkerState workerState) {
        OperationType operation = workerState.operationGenerator.next();
        Integer key = workerState.keyGenerator.next();
        return switch (operation) {
            case GET -> map.get(key);
            case PUT -> map.put(key, workerState.valueGenerator.next());
            case DELETE -> map.remove(key);
        };
    }
}
