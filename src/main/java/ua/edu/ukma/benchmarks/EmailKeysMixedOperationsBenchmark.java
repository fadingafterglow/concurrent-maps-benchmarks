package ua.edu.ukma.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import ua.edu.ukma.generators.Generator;
import ua.edu.ukma.generators.keys.string.UniformEmailKeyGenerator;
import ua.edu.ukma.generators.operations.OperationGenerator;
import ua.edu.ukma.generators.operations.OperationType;
import ua.edu.ukma.generators.values.ValueGenerator;
import ua.edu.ukma.maps.ConcurrentMapType;
import ua.edu.ukma.maps.ConcurrentMapsFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class EmailKeysMixedOperationsBenchmark {

    private static final int PREGENERATION_SIZE = 1_000_000;
    private static final int VALUES_POOL_SIZE = 50;

    @Param
    private ConcurrentMapType mapType;

    @Param("0.5")
    private double prefillFactor;

    @Param({"100:0", "90:5", "70:20", "30:35", "10:45", "0:50"})
    private String operationMix; // e.g., "50:30" means 50% get, 30% put, 20% delete

    @Param("3")
    private int prefixLength;

    @Param("5")
    private int domainsCount;

    @Param("42")
    private long seed;

    private Random random;
    private Map<String, Object> map;

    @Setup(Level.Trial)
    public void setUpTrial() {
        random = new Random(seed);
    }

    @Setup(Level.Iteration)
    public void setUpIteration(BenchmarkParams params) {
        map = ConcurrentMapsFactory.createConcurrentMap(mapType, params.getThreads());
        prefillMap(map);
    }

    private void prefillMap(Map<String, Object> map) {
        String[] keySpace = UniformEmailKeyGenerator.generateKeySpace(prefixLength, domainsCount);
        int prefillSize = (int)(prefillFactor * keySpace.length);
        Collections.shuffle(Arrays.asList(keySpace), random);
        for (int i = 0; i < prefillSize; i++) {
            map.put(keySpace[i], new Object());
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
        private Generator<String> keyGenerator;
        private Generator<OperationType> operationGenerator;
        private Generator<Object> valueGenerator;

        @Setup(Level.Iteration)
        public void setupIteration(EmailKeysMixedOperationsBenchmark benchmark) {
            long keySeed;
            long opSeed;
            synchronized (benchmark) {
                keySeed = benchmark.random.nextLong();
                opSeed = benchmark.random.nextLong();
            }
            keyGenerator = new UniformEmailKeyGenerator(PREGENERATION_SIZE, benchmark.prefixLength, benchmark.domainsCount, keySeed);
            double[] operationProbabilities = Utils.parseOperationMix(benchmark.operationMix);
            operationGenerator = new OperationGenerator(PREGENERATION_SIZE, operationProbabilities[0], operationProbabilities[1], opSeed);
            valueGenerator = new ValueGenerator(VALUES_POOL_SIZE);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object benchmark(WorkerState workerState) {
        OperationType operation = workerState.operationGenerator.next();
        String key = workerState.keyGenerator.next();
        return switch (operation) {
            case GET -> map.get(key);
            case PUT -> map.put(key, workerState.valueGenerator.next());
            case DELETE -> map.remove(key);
        };
    }
}
