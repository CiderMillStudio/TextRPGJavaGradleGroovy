package net.wady.worldmanagement;

import java.util.HashMap;
import java.util.Map;

public class ChunkedWorldMap {

    private final Map<ChunkCoord, Chunk> loaded = new HashMap<>();
    private final ChunkGenerator generator;

    // TEMPORARY CONSTRUCTOR??
    public ChunkedWorldMap(ChunkGenerator generator){
        this.generator = generator;
    }
    // TEMPORARY CONSTRUCTOR

    public Tile tileAt(int worldX, int worldY) {
        int chunkX = Math.floorDiv(worldX, Chunk.SIZE);
        int chunkY = Math.floorDiv(worldY, Chunk.SIZE);
        Chunk chunk = loaded.computeIfAbsent(new ChunkCoord(chunkX, chunkY),
                generator::generate);
        int localX = Math.floorMod(worldX, Chunk.SIZE);
        int localY = Math.floorMod(worldY, Chunk.SIZE);
        return chunk.tileAt(localX, localY);
    }

}
