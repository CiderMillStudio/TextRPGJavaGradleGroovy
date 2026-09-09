package net.wady.worldmanagement;

import net.wady.gameobjects.GameObjectManager;

public class World {
    private final ChunkedWorldMap chunkedWorldMap;
    private final GameObjectManager gameObjectManager;

    public World(ChunkedWorldMap chunkedWorldMap, GameObjectManager gameObjectManager) {
        this.chunkedWorldMap = chunkedWorldMap;
        this.gameObjectManager = gameObjectManager;
    }


    public Tile tileAt(int worldX, int worldY) {
        return chunkedWorldMap.tileAt(worldX, worldY);
    }

    public void worldTick(long deltaTime){
        gameObjectManager.update(deltaTime);
    }
}
