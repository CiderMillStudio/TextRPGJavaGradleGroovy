package net.wady.worldgeneration;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import de.articdive.jnoise.transformers.domain_warp.DomainWarpTransformer;

public class NoisePipelineBuilder {

    private final PerlinNoiseGenerator perlinCosine;
    private final JNoise noisePipeline;

    public NoisePipelineBuilder(long seed, int octaves, double scale, float persistence, float lacunarity) {
        perlinCosine = PerlinNoiseGenerator.newBuilder().setSeed(seed).setInterpolation(Interpolation.COSINE).build();

        noisePipeline = JNoise.newBuilder()
                //.worley(WorleyNoiseGenerator.newBuilder())
                //.addDetailedTransformer(DomainWarpTransformer.newBuilder().setNoiseSource(SuperSimplexNoiseGenerator.newBuilder().build()).build())
                .scale(scale)
                .octavation(perlinCosine,octaves,persistence, lacunarity, FractalFunction.FBM,false)
                .build();
    }

/*    public double evaluateNoise(double x, double y){
        return perlinCosine.evaluateNoise(x, y);
    }*/

    public JNoise getJNoise(){
        return noisePipeline;
    }





}
