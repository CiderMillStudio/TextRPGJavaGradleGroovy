package net.wady.player;

import net.wady.gameobjects.Entity;
import net.wady.physics.Collider;
import net.wady.physics.Vector;
import net.wady.rendering.RenderInfo;
import net.wady.worldmanagement.ChunkCoord;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
   /* private final List<PlayerMoveListener> playerMoveListeners = new ArrayList<>();*/
    // private ChunkCoord playerChunk;


    public Player() {
        // set parameters:
        this.name = "Player_GameObject";
        this.render = new RenderInfo('@', 255,0,255,0,0,0);
        this.position = new Vector(30, 30);

        // define components
        PlayerController playerController = new PlayerController();
        Collider playerCollider = new Collider.Builder().size(new Vector(1, 1)).isTrigger(false).build();

        // add components
        this.addComponent(playerController);
        this.addComponent(playerCollider);


    }

    public void move(Vector movementVector) {
        int currentX = position.getX();
        int currentY = position.getY();

        int newX = currentX + movementVector.getX();
        int newY = currentY + movementVector.getY();

        Vector newPosition = new Vector(newX, newY);

        this.position = newPosition;
    }

    /*public void moveTo(Vector newPosition) {
        Vector oldPosition = this.position;

        this.position = newPosition;
        // this.playerChunk = new ChunkCoord(newPosition);
        // System.out.printf("playerChunk is now: " + playerChunk.getChunkX() + ", " + playerChunk.getChunkY());

        *//*PlayerMoveEvent event = new PlayerMoveEvent(oldPosition, newPosition, oldChunk, playerChunk);
        for (PlayerMoveListener listener : playerMoveListeners) {
            listener.onPlayerMove(event);
        }*//*
    }*/

/*    public void addMoveListener(PlayerMoveListener playerMoveListener) {
        playerMoveListeners.add(playerMoveListener);
    }

    public void removeMoveListener(PlayerMoveListener playerMoveListener) {
        playerMoveListeners.remove(playerMoveListener);
    }*/




}
