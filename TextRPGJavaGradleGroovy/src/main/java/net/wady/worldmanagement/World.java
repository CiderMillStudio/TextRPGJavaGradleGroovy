package net.wady.worldmanagement;

import net.wady.gameobjects.GameObjectManager;
import net.wady.player.Player;

public class World {
    private final ChunkedWorldMap chunkedWorldMap;
    private final GameObjectManager gameObjectManager = new GameObjectManager();

    private Player player = new Player();

    public World(long worldSeed) {

        this.chunkedWorldMap = new ChunkedWorldMap(worldSeed);
        //player.addMoveListener(chunkedWorldMap);
    }


    public Tile tileAt(int worldX, int worldY) {
        return chunkedWorldMap.tileAt(worldX, worldY);
    }

    // A World Tick updates everything in the world (gameObjects, etc...)
    public void worldTick(long deltaTime){
        gameObjectManager.update(deltaTime);
    }

    // TEMPORARY , NEED TO RELOCATE THIS
    public void SpawnPlayer() {
        gameObjectManager.spawn(player);
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }
}
