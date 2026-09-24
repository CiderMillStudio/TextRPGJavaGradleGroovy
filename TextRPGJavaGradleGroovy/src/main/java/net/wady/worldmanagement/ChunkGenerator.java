package net.wady.worldmanagement;

import net.wady.worldgeneration.Hasher;
import net.wady.worldgeneration.NoisePipelineBuilder;
import net.wady.worldgeneration.NoiseRangeFinder;

public class ChunkGenerator {
    private final long worldSeed;
    private NoisePipelineBuilder overworldTerrainNoise;
    //private NoisePipelineBuilder overworldCorruptabilityNoise;

    private double normalizeValue;

    private int numOfOctaves = 7;
    private float persistence = 0.8f; // The percentage of each increasing octave's wavelength that gets added to the noise wave
    private float peakFactor = 0.4f; // the closer this value is to 0, the more 'peaks and valleys' (extremes) there will be. If this is set to 1, there will be very few cases of the extremes

    public ChunkGenerator(long worldSeed) {
        this.worldSeed = worldSeed;

        setUpNoisePipelines(worldSeed);
    }


    private void setUpNoisePipelines(long worldSeed) {

        this.overworldTerrainNoise = new NoisePipelineBuilder(Hasher.mix(worldSeed), numOfOctaves, 0.01, persistence, 1.65f);

        NoiseRangeFinder rangeFinder = new NoiseRangeFinder();
        double[] minMax = rangeFinder.findRange(overworldTerrainNoise.getJNoise(), 4_000_000, 100_000);
        Math.clamp(peakFactor, 0, 1);
        normalizeValue = peakFactor * minMax[1];  // [0] is the minimum, [1] is the maximum ; peakFactor is a float that influences how likely 'peaks and troughs' are to form

        // System.out.println("Chunk Generator: normalizeValue is = " + normalizeValue);
        // System.out.println("Min and Max are, respectively: " + minMax[0] + ", " + minMax[1] );

    }

    Chunk generate(ChunkCoord coord) {

        byte[] terrainTypes = new byte[Chunk.SIZE * Chunk.SIZE];

        int startingX = coord.chunkX() * Chunk.SIZE;
        int startingY = coord.chunkY() * Chunk.SIZE;

        int currentX = startingX;
        int currentY = startingY;

        int rowCount = 0;
        int colCount = 0;

        double maximumPossibleValue = 2 * normalizeValue;
        System.out.println("\n\nCHUNK GENERATOR: maximumPossibleValue is = " + maximumPossibleValue);

        for (int i = 0; i < terrainTypes.length; i++) {
            double noise = overworldTerrainNoise.getJNoise().evaluateNoise(currentX, currentY);
            double normalizedNoise = (noise + normalizeValue);

            normalizedNoise = Math.clamp(normalizedNoise, 0.0, maximumPossibleValue); // Now we have a value between 0.0 and the Maximum Possible Value;

            normalizedNoise = normalizedNoise / maximumPossibleValue;

            int amplifiedNoise = (int) Math.round(normalizedNoise * 4f);

            int elevationValue = Math.round((float) amplifiedNoise);

            byte terrainByte = (byte) Math.round(elevationValue);

            terrainTypes[i] = terrainByte;


            if (colCount >= Chunk.SIZE - 1) {
                //terrainTypes[i] = 4; // activating this comment allows you to see the X-borders of each chunk
                currentX = startingX;
                colCount = 0;
                currentY++;
                rowCount++;

            } else {
                currentX++;
                colCount++;
            }

            if (rowCount + 1 == Chunk.SIZE) {
                //terrainTypes[i] = 4; // activating this comment allows you to see the Y-borders of each chunk
            }

            /*if (rowCount >= Chunk.SIZE) {
                System.out.println("DONE WITH CHUNK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }*/

            /*          System.out.println("raw noise: " + noise);
            System.out.println("CHUNK GENERATOR: normalized Noise: " + normalizedNoise);
            System.out.println("CHUNK GENERATOR: amplifiedNoise = " + amplifiedNoise);
            System.out.println("CHUNK GENERATOR: terrainByte: " + terrainByte);
            System.out.println("worldCoords: " + currentX + ", " + currentY);
            System.out.println("Chunk Coords:" + coord.chunkX() + ", " + coord.chunkY());*/
        }


        System.out.println("CHUNK GENERATOR: now loading chunk: " + coord.toString());

        return new Chunk(coord, terrainTypes);

    }


}
