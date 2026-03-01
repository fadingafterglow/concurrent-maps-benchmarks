package ua.edu.ukma.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import ua.edu.ukma.generators.Generator;
import ua.edu.ukma.generators.keys.integer.IntegerKeyDistribution;
import ua.edu.ukma.generators.keys.integer.IntegerKeyGeneratorsFactory;
import ua.edu.ukma.generators.values.ValueGenerator;
import ua.edu.ukma.maps.ConcurrentMapType;
import ua.edu.ukma.maps.ConcurrentMapsFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.SampleTime})
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class IntegerKeysGroupedOperationsBenchmark {

    private static final int PREGENERATION_SIZE = 1_000_000;
    private static final int DISTRIBUTION_ACCURATE_PREFILL_ATTEMPTS = 5;
    private static final int VALUES_POOL_SIZE = 50;

    @Param
    private ConcurrentMapType mapType;

    @Param("0.5")
    private double prefillFactor;

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
    public static class GetOrDeleteWorkerState {
        private Generator<Integer> keyGenerator;

        @Setup(Level.Iteration)
        public void setupIteration(IntegerKeysGroupedOperationsBenchmark benchmark) {
            long keySeed;
            synchronized (benchmark) {
                keySeed = benchmark.random.nextLong();
            }
            keyGenerator = IntegerKeyGeneratorsFactory
                    .createIntegerKeyGenerator(benchmark.keyDistribution, PREGENERATION_SIZE, benchmark.keyRange, keySeed);
        }
    }

    @State(Scope.Thread)
    public static class PutWorkerState {
        private Generator<Integer> keyGenerator;
        private Generator<Object> valueGenerator;

        @Setup(Level.Iteration)
        public void setupIteration(IntegerKeysGroupedOperationsBenchmark benchmark) {
            long keySeed;
            synchronized (benchmark) {
                keySeed = benchmark.random.nextLong();
            }
            keyGenerator = IntegerKeyGeneratorsFactory
                    .createIntegerKeyGenerator(benchmark.keyDistribution, PREGENERATION_SIZE, benchmark.keyRange, keySeed);
            valueGenerator = new ValueGenerator(VALUES_POOL_SIZE);
        }
    }

    @Group("g12_3_1")
    @GroupThreads(12)
    @Benchmark
    public Object get12(GetOrDeleteWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.get(key);
    }

    @Group("g12_3_1")
    @GroupThreads(3)
    @Benchmark
    public Object put3(PutWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        Object value = workerState.valueGenerator.next();
        return map.put(key, value);
    }

    @Group("g12_3_1")
    @GroupThreads(1)
    @Benchmark
    public Object remove1(GetOrDeleteWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.remove(key);
    }

    @Group("g6_5_5")
    @GroupThreads(6)
    @Benchmark
    public Object get6(GetOrDeleteWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.get(key);
    }

    @Group("g6_5_5")
    @GroupThreads(5)
    @Benchmark
    public Object put5(PutWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        Object value = workerState.valueGenerator.next();
        return map.put(key, value);
    }

    @Group("g6_5_5")
    @GroupThreads(5)
    @Benchmark
    public Object remove5(GetOrDeleteWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.remove(key);
    }

    @Group("g2_7_7")
    @GroupThreads(2)
    @Benchmark
    public Object get2(GetOrDeleteWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.get(key);
    }

    @Group("g2_7_7")
    @GroupThreads(7)
    @Benchmark
    public Object put7(PutWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        Object value = workerState.valueGenerator.next();
        return map.put(key, value);
    }

    @Group("g2_7_7")
    @GroupThreads(7)
    @Benchmark
    public Object remove7(GetOrDeleteWorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.remove(key);
    }
}
