package net.wady.data;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    PlayerData playerData = new PlayerData();
    WorldData worldData = new WorldData();

    public GameData() {

    }


    public PlayerData getPlayerData() {
        return playerData;
    }

    public WorldData getWorldData() {
        return worldData;
    }
}