package net.wady.worldmanagement;

import net.wady.player.playerevents.PlayerMoveEvent;
import net.wady.player.playerevents.PlayerMoveListener;

import java.util.HashMap;
import java.util.Map;

public class ChunkedWorldMap implements PlayerMoveListener {

    private static final int CHUNK_LOAD_RADIUS = 2;
    private static final int CHUNK_UNLOAD_RADIUS = 5;

    private final Map<ChunkCoord, Chunk> loaded = new HashMap<>();
    private final ChunkGenerator generator;
    private ChunkCoord playerChunk;


    public ChunkedWorldMap(long worldSeed){
        this.generator = new ChunkGenerator(worldSeed);
    }

    public Tile tileAt(int worldX, int worldY) {
        int chunkX = Math.floorDiv(worldX, Chunk.SIZE);
        int chunkY = Math.floorDiv(worldY, Chunk.SIZE);
        Chunk chunk = loaded.computeIfAbsent(new ChunkCoord(chunkX, chunkY), // should eventually be able to get rid of 'computeIfAbsent'
                generator::generate);
        int localX = Math.floorMod(worldX, Chunk.SIZE);
        int localY = Math.floorMod(worldY, Chunk.SIZE);
        return chunk.tileAt(localX, localY);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.hasChangedChunk() == false) return;

        //else:
        this.playerChunk = event.getNewChunk();
        loadChunksAroundPlayer();
        unloadDistantChunks();

    }


    // need to implement below code later:

    private void loadChunksAroundPlayer() {
        for (int dx = -CHUNK_LOAD_RADIUS; dx <= CHUNK_LOAD_RADIUS; dx++ ) {
            for (int dy = -CHUNK_LOAD_RADIUS; dy <= CHUNK_LOAD_RADIUS; dy++) {
                ChunkCoord coord = new ChunkCoord(playerChunk.chunkX() + dx, playerChunk.chunkY() + dy);
                if (playerChunk.getChunkDistanceTaxicab(coord) <= CHUNK_LOAD_RADIUS)
                {
                    loaded.computeIfAbsent(coord, generator::generate);
                }
            }
        }
    }

    private void unloadDistantChunks() {
        // CONSIDER SAVING CHUNK DATA TO DISK BEFORE UNLOADING, ONCE WE WANT PERMANENCE.

        loaded.keySet().removeIf(coord -> coord.getChunkDistanceTaxicab(playerChunk) > CHUNK_UNLOAD_RADIUS);
        System.out.println(this.toString() + ": Number of remaining loaded chunks: \n " + loaded.keySet().size());
    }
}
