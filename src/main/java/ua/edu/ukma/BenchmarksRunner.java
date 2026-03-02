package ua.edu.ukma;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarksRunner {

    private static final int[] THREAD_COUNTS = {1, 4, 8, 16, 32};

    public static void main(String[] args) throws RunnerException {
        Options baseOpts = new OptionsBuilder()
                .forks(3)
                .shouldDoGC(true)
                .jvmArgsAppend("-Xms512m", "-Xmx8g")
                .build();
        //runMixed(baseOpts);
        //runGrouped(baseOpts);
        //runFill(baseOpts);
        runMemory(baseOpts);
    }

    private static void runMixed(Options baseOpts) throws RunnerException {
        for (int threads : THREAD_COUNTS) {
            Options opt = new OptionsBuilder()
                    .parent(baseOpts)
                    .include("ua.edu.ukma.benchmarks.*MixedOperations*")
                    .result(threads + "-threads-results.csv")
                    .threads(threads)
                    .build();
            new Runner(opt).run();
        }
    }

    private static void runGrouped(Options baseOpts) throws RunnerException {
        Options opt = new OptionsBuilder()
                .parent(baseOpts)
                .include("ua.edu.ukma.benchmarks.*GroupedOperations*")
                .result("grouped-results.csv")
                .threads(16)
                .build();
        new Runner(opt).run();
    }

    private static void runFill(Options baseOpts) throws RunnerException {
        Options opt = new OptionsBuilder()
                .parent(baseOpts)
                .include("ua.edu.ukma.benchmarks.*Fill*")
                .result("warm-fill-results.csv")
                .build();
        new Runner(opt).run();

        opt = new OptionsBuilder()
                .parent(baseOpts)
                .include("ua.edu.ukma.benchmarks.*Fill*")
                .result("cold-fill-results.csv")
                .forks(15)
                .warmupIterations(0)
                .measurementIterations(1)
                .build();
        new Runner(opt).run();
    }

    private static void runMemory(Options baseOpts) throws RunnerException {
        Options opt = new OptionsBuilder()
                .parent(baseOpts)
                .include("ua.edu.ukma.benchmarks.*Memory*")
                .result("memory-results.csv")
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
