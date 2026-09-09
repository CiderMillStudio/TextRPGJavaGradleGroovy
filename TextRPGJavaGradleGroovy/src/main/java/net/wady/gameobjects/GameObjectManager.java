package net.wady.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class GameObjectManager {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> pending = new ArrayList<>();

    public void spawn(GameObject go) {
        pending.add(go); // by adding to pending, we ensure the go does not spawn mid-frame
    }

    public void update(long deltaTime) {
        // By BATCHING, we first call the necessary AWAKE methods for ALL pending gameobjects,
        for (GameObject gameObject : pending) {
            gameObject.awake();
        }

        // and then we call the necessary START methods for ALL pending game objects
        for (GameObject gameObject : pending) {
            gameObject.start();
        }
        // then we add all pending GO's to the gameObject's list for our scene and CLEAR the pending list of GO's
        gameObjects.addAll(pending);
        pending.clear();

        // now that ALL GO's have fired their awakes, then their starts, now we can fire their
        // UPDATE methods :)
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}
