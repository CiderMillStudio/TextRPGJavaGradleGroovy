package net.wady.worldgeneration;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.core.api.modifiers.NoiseModifier;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;

public class NoiseGenerator {

    private final PerlinNoiseGenerator perlinCosine;
    private final JNoise noisePipeline;

    public NoiseGenerator(long seed, int octaves, double scale, float persistence, float lacunarity) {
        perlinCosine = PerlinNoiseGenerator.newBuilder().setSeed(seed).setInterpolation(Interpolation.LINEAR).build();

        // In most cases, one would inline the perlinCosine value into the builder chain.
        noisePipeline = JNoise.newBuilder()
                .scale(scale)
                .octavation(perlinCosine,octaves,persistence, lacunarity, FractalFunction.FBM,false)
                .build();
    }

    public double evaluateNoise(double x, double y){
        return perlinCosine.evaluateNoise(x, y);
    }

    public JNoise getJNoise(){
        return noisePipeline;
    }



}
