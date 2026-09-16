package net.wady.worldmanagement;

import net.wady.physics.Vector;

public record ChunkCoord(int chunkX, int chunkY) {
// making something a record (FINAL class with immutable parameters) automatically generates getters,
// equals(), hashCode, and toString!


    public ChunkCoord(Vector worldPosition) {
        int worldX = worldPosition.x();
        int worldY = worldPosition.y();

        int x = Math.floorDiv(worldX, Chunk.SIZE);
        int y = Math.floorDiv(worldY, Chunk.SIZE);

        // System.out.println("CHUNKCOORD: At World Position (" + worldX + ", " + worldY + ")," + "\nChunkCoord is (" + x + ", " + y + ").");

        this(x, y);
    }

    public int getChunkDistanceTaxicab(ChunkCoord theOtherChunk) {

        int otherChunkX = theOtherChunk.chunkX;
        int otherChunkY = theOtherChunk.chunkY;

        int dx = this.chunkX - otherChunkX;
        int dy = this.chunkY - otherChunkY;

        return (Math.abs(dx) + Math.abs(dy));

    }
}
