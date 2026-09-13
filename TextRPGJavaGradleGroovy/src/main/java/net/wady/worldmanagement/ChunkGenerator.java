package net.wady.worldmanagement;

public class ChunkGenerator {
    private final long worldSeed;

    public ChunkGenerator(long worldSeed) {
        this.worldSeed = worldSeed;
    }

    Chunk generate(ChunkCoord coord) {

        return new Chunk();
    }


}
