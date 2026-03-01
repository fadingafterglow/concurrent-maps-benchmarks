package ua.edu.ukma;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarksRunner {

    public static void main(String[] args) throws RunnerException {
        for (int threads : new int[]{1, 4, 8, 16, 32}) {
            Options opt = new OptionsBuilder()
                    .include("ua.edu.ukma.benchmarks.*")
                    .result(threads + "-threads-results.csv")
                    .forks(3)
                    .threads(threads)
                    .shouldDoGC(true)
//                    .param("mapType", "SYNCHRONIZED_HASH_MAP")
//                    .param("operationMix", "95:3")
//                    .param("keyDistribution", "UNIFORM")
//                    .param("keyRange", "1000000")
                    .jvmArgsAppend("-Xms512m", "-Xmx8g")
                    .build();
            new Runner(opt).run();
        }
    }
}
