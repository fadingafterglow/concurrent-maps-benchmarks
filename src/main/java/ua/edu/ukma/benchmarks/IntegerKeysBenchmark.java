package ua.edu.ukma.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import ua.edu.ukma.maps.ConcurrentMapType;

@State(Scope.Benchmark)
public class ConcurrentMapsBenchmark {

    @Param
    public ConcurrentMapType type;



    @Benchmark
    public void testMethod() {
        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
        // Put your benchmark code here.
    }

}
