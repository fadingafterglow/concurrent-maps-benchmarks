package ua.edu.ukma.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jol.info.GraphLayout;
import ua.edu.ukma.maps.ConcurrentMapType;
import ua.edu.ukma.maps.ConcurrentMapsFactory;

import java.util.Map;

@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class MemoryFootprintBenchmark {

    @Param
    private ConcurrentMapType mapType;

    @Param({"0", "100", "10000", "1000000"})
    private int size;

    private Map<Integer, Object> map;

    @Setup(Level.Iteration)
    public void setUpIteration() {
        map = ConcurrentMapsFactory.createConcurrentMap(mapType, 16);
    }

    @TearDown(Level.Iteration)
    public void tearDownIteration() {
        map = null;
    }

    @State(Scope.Thread)
    @AuxCounters(AuxCounters.Type.EVENTS)
    public static class Measurement {
        public long totalSize;
    }

    @Benchmark
    public void putsOnly(Measurement measurement) {
        for (int i = 0; i < size; i++) {
            map.put(i, new Object());
        }
        measurement.totalSize = GraphLayout.parseInstance(map).totalSize();
    }

    @Benchmark
    public void putsThenRemoves(Measurement measurement) {
        for (int i = 0; i < 4 * size; i++) {
            map.put(i, new Object());
        }
        for (int i = 0; i < 4 * size; i++) {
            if (i % 4 == 0) continue;
            map.remove(i);
        }
        measurement.totalSize = GraphLayout.parseInstance(map).totalSize();
    }

    @Benchmark
    public void putsThenRemovesThenPuts(Measurement measurement) {
        for (int i = 0; i < 2 * size; i++) {
            map.put(i, new Object());
        }
        for (int i = 0; i < 2 * size; i++) {
            if (i % 4 == 0) continue;
            map.remove(i);
        }
        for (int i = 3; i < 2 * size; i += 4) {
            map.put(i, new Object());
        }
        measurement.totalSize = GraphLayout.parseInstance(map).totalSize();
    }
}
