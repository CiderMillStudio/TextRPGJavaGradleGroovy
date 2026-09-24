package net.wady.worldgeneration;

import de.articdive.jnoise.pipeline.JNoise;

// The purpose of this class is to allow the ChunkGenerator system to identify the relative peak and trough values
// of a given JNoise generator. Can take millions of samples in a given area and then reports double array containing
// solely the min and max values obtained from the sampling of the findRange() method.

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
