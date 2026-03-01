package ua.edu.ukma.benchmarks;

public class Utils {

    private Utils() {}

    public static double[] parseOperationMix(String mix) {
        String[] parts = mix.split(":");
        double getRatio = Double.parseDouble(parts[0]) / 100.0;
        double putRatio = Double.parseDouble(parts[1]) / 100.0;
        double deleteRatio = 1.0 - getRatio - putRatio;
        return new double[]{getRatio, putRatio, deleteRatio};
    }
}
