package net.wady.worldmanagement;

import com.sun.prism.paint.Color;
import net.wady.worldgeneration.NoisePipelineBuilder;
import net.wady.worldgeneration.NoiseRangeFinder;

public class ChunkGenerator {
    private final long worldSeed;
    private NoisePipelineBuilder overworldTerrainNoise;
    //private NoisePipelineBuilder overworldCorruptabilityNoise;

    private int numOfOctaves = 6;
    private float persistence = 0.5f;
    private float peakFactor = 0.3f; // the closer this value is to 0, the more 'peaks and valleys' (extremes) there will be. If this is set to 1, there will be very few cases of the extremes
    private double normalizeValue;

    public ChunkGenerator(long worldSeed) {
        this.worldSeed = worldSeed;

        setUpNoisePipelines(worldSeed);
    }

    private void setUpNoisePipelines(long worldSeed) {
        long adder = 345;

        this.overworldTerrainNoise = new NoisePipelineBuilder(hash(worldSeed), numOfOctaves, 0.1, persistence, 1.65f);


        /*for (int i = 0; i < numOfOctaves; i++) {
            normalizeValue += (0.5 * Math.pow(persistence, i));
        }*/

        NoiseRangeFinder rangeFinder = new NoiseRangeFinder();
        double[] minMax = rangeFinder.findRange(overworldTerrainNoise.getJNoise(), 4_000_000, 10_000);
        // System.out.println("Min and Max are, respectively: " + minMax[0] + ", " + minMax[1] );

        Math.clamp(peakFactor, 0, 1);

        normalizeValue = peakFactor * minMax[1];  // [0] is the minimum, [1] is the maximum

        System.out.println("Chunk Generator: normalizeValue is = " + normalizeValue);

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

            //System.out.println("CHUNK GENERATOR: pre-clamped normalizedNoise = " + normalizedNoise);

            double preclamped = normalizedNoise;

            normalizedNoise = Math.clamp(normalizedNoise, 0.0, maximumPossibleValue); // Now we have a value between 0.0 and the Maximum Possible Value;

            double postclamped = normalizedNoise;
            //System.out.println("CHUNK GENERATOR: post-clamped normalizedNoise = " + normalizedNoise);

            normalizedNoise = normalizedNoise / maximumPossibleValue;


            /*if (postclamped != preclamped) {
                System.out.println( "\nCHUNK GENERATOR: pre-clamped normalizedNoise = " + preclamped + "\nCHUNK GENERATOR: post-clamped normalizedNoise = " + postclamped);
            }*/


            int amplifiedNoise = (int) Math.round(normalizedNoise * 4f);


            int elevationValue = Math.round((float) amplifiedNoise);

            byte terrainByte = (byte) Math.round(elevationValue);

            terrainTypes[i] = terrainByte;

/*            System.out.println("raw noise: " + noise);
            System.out.println("CHUNK GENERATOR: preclamped Noise: " + preclamped);
            System.out.println("CHUNK GENERATOR: postclamped Noise: " + postclamped);
            System.out.println("CHUNK GENERATOR: normalized Noise: " + normalizedNoise);
            System.out.println("CHUNK GENERATOR: amplifiedNoise = " + amplifiedNoise);
            System.out.println("CHUNK GENERATOR: terrainByte: " + terrainByte);
            System.out.println("worldCoords: " + currentX + ", " + currentY);
            System.out.println("Chunk Coords:" + coord.chunkX() + ", " + coord.chunkY());*/

         /*   if (normalizedNoise < 0) {
                System.out.println("NOISE IS NEGATIVE\n\n");
            }*/

            if (colCount >= 63) {
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

            if (rowCount >= Chunk.SIZE) {
                System.out.println("DONE WITH CHUNK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }


        System.out.println("CHUNK GENERATOR: new chunk loading " + coord.toString());
        return new Chunk(coord, terrainTypes);

    }



    private long hash(long hashable) {
        long hashed = hashable % 639;
        return hashed;
    }


}
