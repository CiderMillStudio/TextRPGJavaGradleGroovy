package net.wady.worldmanagement;

public class Chunk {
static final int SIZE = 64;
private final byte[] terrainType /*= new byte[SIZE * SIZE]*/; // all zero by default = TerrainType.values()[0] = FLOOR
private final ChunkCoord coord;


public Chunk(ChunkCoord coord, byte[] terrainType) {
    this.coord = coord;
    this.terrainType = terrainType;
    /*System.out.println("CHUNK: terrainType.length:" + terrainType.length);
    System.out.println("CHUNK:" + coord.toString());
    // for (int i = 0; i < terrainType.length; i++) System.out.println(terrainType[i]);
    System.out.println("CHUNK: terrainType length is " + terrainType.length);*/

}

Tile tileAt(int localX, int localY) {
    //System.out.println("CHUNK: ");
    TerrainType t = TerrainType.values()[terrainType[localY * SIZE + localX] & 0xFF];
    return new Tile(t);
}



}
