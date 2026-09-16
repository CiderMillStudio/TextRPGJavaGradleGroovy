package net.wady.worldmanagement;

public class ChunkGenerator {
    private final long worldSeed;

    public ChunkGenerator(long worldSeed) {
        this.worldSeed = worldSeed;
    }

    Chunk generate(ChunkCoord coord) {
        // System.out.println(this.toString() + ": generating new chunk: " + coord.toString());
        return new Chunk(coord);
    }


}
