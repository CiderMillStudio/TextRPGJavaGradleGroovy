package net.wady.worldmanagement;

public class Chunk {
static final int SIZE = 64;
private final byte[] terrainType = new byte[SIZE * SIZE]; // all zero by default = TerrainType.values()[0] = FLOOR
private final ChunkCoord coord;


public Chunk(ChunkCoord coord) {
    this.coord = coord;
}

Tile tileAt(int localX, int localY) {
    TerrainType t = TerrainType.values()[terrainType[localY * SIZE + localX] & 0xFF];
    return new Tile(t);
}



}
