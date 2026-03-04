package ua.edu.ukma;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ua.edu.ukma.benchmarks.*;

public class BenchmarksRunner {

    private static final int[] THREAD_COUNTS = {1, 4, 8, 16, 32};

    public static void main(String[] args) throws RunnerException {
        Options baseOpts = new OptionsBuilder()
                .forks(3)
                .shouldDoGC(true)
                .jvmArgsAppend("-Xms512m", "-Xmx8g")
                .build();
        runIntegerKeysMixedOperationsBenchmark(baseOpts);
        runIntegerKeysGroupedOperationsBenchmark(baseOpts);
        runIntegerKeysFillBenchmark(baseOpts);
        runMemoryFootprintBenchmark(baseOpts);
        runEmailKeysMixedOperationsBenchmark(baseOpts);
    }

    private static void runIntegerKeysMixedOperationsBenchmark(Options baseOpts) throws RunnerException {
        for (int threads : THREAD_COUNTS) {
            Options opt = new OptionsBuilder()
                    .parent(baseOpts)
                    .include(IntegerKeysMixedOperationsBenchmark.class.getName())
                    .result("int-mixed-" + threads + "-threads-results.csv")
                    .threads(threads)
                    .build();
            new Runner(opt).run();
        }
    }

    private static void runIntegerKeysGroupedOperationsBenchmark(Options baseOpts) throws RunnerException {
        Options opt = new OptionsBuilder()
                .parent(baseOpts)
                .include(IntegerKeysGroupedOperationsBenchmark.class.getName())
                .result("int-grouped-results.csv")
                .threads(16)
                .build();
        new Runner(opt).run();
    }

    private static void runIntegerKeysFillBenchmark(Options baseOpts) throws RunnerException {
        Options opt = new OptionsBuilder()
                .parent(baseOpts)
                .include(IntegerKeysFillBenchmark.class.getName())
                .result("int-warm-fill-results.csv")
                .build();
        new Runner(opt).run();

        opt = new OptionsBuilder()
                .parent(baseOpts)
                .include(IntegerKeysFillBenchmark.class.getName())
                .result("int-cold-fill-results.csv")
                .forks(15)
                .warmupIterations(0)
                .measurementIterations(1)
                .build();
        new Runner(opt).run();
    }

    private static void runMemoryFootprintBenchmark(Options baseOpts) throws RunnerException {
        Options opt = new OptionsBuilder()
                .parent(baseOpts)
                .include(MemoryFootprintBenchmark.class.getName())
                .result("memory-results.csv")
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    private static void runEmailKeysMixedOperationsBenchmark(Options baseOpts) throws RunnerException {
        for (int threads : THREAD_COUNTS) {
            Options opt = new OptionsBuilder()
                    .parent(baseOpts)
                    .include(EmailKeysMixedOperationsBenchmark.class.getName())
                    .result("email-mixed-" + threads + "-threads-results.csv")
                    .threads(threads)
                    .build();
            new Runner(opt).run();
        }
    }
}
