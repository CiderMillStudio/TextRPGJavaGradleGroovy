package net.wady.worldgeneration;

public class NoiseGenerator {

    public void makeTestNoise(long seed, int octaves, float scale, float persistence, float lacunarity) {
        NoisePipelineBuilder noise = new NoisePipelineBuilder(seed, octaves, scale, persistence, lacunarity);

        NoiseImageSaver noiseImageSaver = new NoiseImageSaver();

        int chunkIntMultiplier = 3;

        noiseImageSaver.savePixelatedNoise(noise.getJNoise(), "pixelated_noise7.png", (64 * chunkIntMultiplier), (64 * chunkIntMultiplier), 1);

    }




}
