package net.wady.worldmanagement;

public class Tile {


    private final TerrainType terrainType;

    public Tile(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public TerrainType terrainType() {
        return terrainType;
    }
}
