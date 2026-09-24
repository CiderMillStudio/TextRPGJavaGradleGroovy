package net.wady.worldgeneration;

import de.articdive.jnoise.pipeline.JNoise;

public class NoiseRangeFinder {
    public static double[] findRange(JNoise noise, int samples, double area) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < samples; i++) {
            double x = Math.random() * area - (area / 2f);
            double y = Math.random() * area - (area / 2f);
            double v = noise.evaluateNoise(x, y);
            min = Math.min(min, v);
            max = Math.max(max, v);
        }

        return new double[]{min, max};
    }
}
