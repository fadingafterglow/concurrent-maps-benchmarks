package ua.edu.ukma.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import ua.edu.ukma.generators.Generator;
import ua.edu.ukma.generators.keys.integer.ShuffledRangeIntegerKeyGenerator;
import ua.edu.ukma.maps.ConcurrentMapType;
import ua.edu.ukma.maps.ConcurrentMapsFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class IntegerKeysFillBenchmark {

    @Param
    private ConcurrentMapType mapType;

    @Param("42")
    private long seed;

    private Random random;
    private int nextRange;
    private Map<Integer, Object> map;

    @Setup(Level.Trial)
    public void setUpTrial() {
        random = new Random(seed);
    }

    @Setup(Level.Iteration)
    public void setUpIteration(BenchmarkParams params) {
        nextRange = 0;
        map = ConcurrentMapsFactory.createConcurrentMap(mapType, params.getThreads());
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
        private Object value;

        @Setup(Level.Iteration)
        public void setupIteration(IntegerKeysFillBenchmark benchmark, BenchmarkParams params) {
            long keySeed;
            int range;
            synchronized (benchmark) {
                keySeed = benchmark.random.nextLong();
                range = benchmark.nextRange++;
            }
            int perThreadPuts = params.getMeasurement().getBatchSize();
            keyGenerator = new ShuffledRangeIntegerKeyGenerator(perThreadPuts, range * perThreadPuts, keySeed);
            value = new Object();
        }
    }

    @Benchmark
    @Threads(1)
    @Warmup(batchSize = 16384 / 1)
    @Measurement(batchSize = 16384 / 1)
    public Object fill_1_16384(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(4)
    @Warmup(batchSize = 16384 / 4)
    @Measurement(batchSize = 16384 / 4)
    public Object fill_4_16384(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(8)
    @Warmup(batchSize = 16384 / 8)
    @Measurement(batchSize = 16384 / 8)
    public Object fill_8_16384(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(16)
    @Warmup(batchSize = 16384 / 16)
    @Measurement(batchSize = 16384 / 16)
    public Object fill_16_16384(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(32)
    @Warmup(batchSize = 16384 / 32)
    @Measurement(batchSize = 16384 / 32)
    public Object fill_32_16384(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(1)
    @Warmup(batchSize = 1048576 / 1)
    @Measurement(batchSize = 1048576 / 1)
    public Object fill_1_1048576(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(4)
    @Warmup(batchSize = 1048576 / 4)
    @Measurement(batchSize = 1048576 / 4)
    public Object fill_4_1048576(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(8)
    @Warmup(batchSize = 1048576 / 8)
    @Measurement(batchSize = 1048576 / 8)
    public Object fill_8_1048576(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(16)
    @Warmup(batchSize = 1048576 / 16)
    @Measurement(batchSize = 1048576 / 16)
    public Object fill_16_1048576(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(32)
    @Warmup(batchSize = 1048576 / 32)
    @Measurement(batchSize = 1048576 / 32)
    public Object fill_32_1048576(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(1)
    @Warmup(batchSize = 16777216 / 1)
    @Measurement(batchSize = 16777216 / 1)
    public Object fill_1_16777216(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(4)
    @Warmup(batchSize = 16777216 / 4)
    @Measurement(batchSize = 16777216 / 4)
    public Object fill_4_16777216(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(8)
    @Warmup(batchSize = 16777216 / 8)
    @Measurement(batchSize = 16777216 / 8)
    public Object fill_8_16777216(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(16)
    @Warmup(batchSize = 16777216 / 16)
    @Measurement(batchSize = 16777216 / 16)
    public Object fill_16_16777216(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }

    @Benchmark
    @Threads(32)
    @Warmup(batchSize = 16777216 / 32)
    @Measurement(batchSize = 16777216 / 32)
    public Object fill_32_16777216(WorkerState workerState) {
        Integer key = workerState.keyGenerator.next();
        return map.put(key, workerState.value);
    }
}
