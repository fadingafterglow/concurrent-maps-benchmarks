package ua.edu.ukma;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarksRunner {

    public static void main(String[] args) throws RunnerException {
        Options baseOpts = new OptionsBuilder()
                .forks(3)
                .shouldDoGC(true)
                .jvmArgsAppend("-Xms512m", "-Xmx8g")
                .build();
        //runMixed(baseOpts);
        runGrouped(baseOpts);
    }

    private static void runMixed(Options baseOpts) throws RunnerException {
        for (int threads : new int[]{1, 4, 8, 16, 32}) {
            Options opt = new OptionsBuilder()
                    .parent(baseOpts)
                    .include("ua.edu.ukma.benchmarks.*MixedOperations*")
                    .result(threads + "-threads-results.csv")
                    .threads(threads)
//                    .param("mapType", "SYNCHRONIZED_HASH_MAP", "RW_SYNCHRONIZED_HASH_MAP")
//                    .param("operationMix", "95:3")
//                    .param("keyDistribution", "UNIFORM")
//                    .param("keyRange", "1000000")
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
//                .param("keyDistribution", "UNIFORM")
//                .param("keyRange", "1000000")
                .build();
        new Runner(opt).run();
    }
}
