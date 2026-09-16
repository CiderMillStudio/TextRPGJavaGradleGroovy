package net.wady.player.playerevents;

import net.wady.physics.Vector;
import net.wady.worldmanagement.ChunkCoord;

public class PlayerMoveEvent {
    private final Vector oldPosition;
    private final Vector newPosition;
    private final ChunkCoord oldChunk;
    private final ChunkCoord newChunk;
    private final Vector moveDirection;

    public PlayerMoveEvent(Vector oldPosition, Vector newPosition, ChunkCoord oldChunk, ChunkCoord newChunk) {
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.oldChunk = oldChunk;
        this.newChunk = newChunk;
        this.moveDirection = new Vector (newPosition.x() - oldPosition.x(), newPosition.y() - oldPosition.y());

        /*if (hasChangedChunk()) {
            System.out.printf(this.toString() + ": playerChunk is now: " + newChunk.chunkX() + ", " + newChunk.chunkY());
        }*/

        if (hasChangedPosition(oldPosition, newPosition)) {
            System.out.println("PlayerMoveEvent:  playerPosition = " + newPosition.x() + ", " + newPosition.y());
        }

    }

    public boolean hasChangedChunk() {
        return !oldChunk.equals(newChunk);
    }

    public boolean hasChangedPosition(Vector oldPosition, Vector newPosition) {
        if (oldPosition.y() == newPosition.y()) {
            if (oldPosition.x() == newPosition.x()) {
                return false;
            }
        }
        return true;
    }

    public ChunkCoord getOldChunk() { return oldChunk; }
    public ChunkCoord getNewChunk() { return newChunk; }
    public Vector getNewPosition() { return newPosition; }
    public Vector getMoveDirection() { return moveDirection; }


}
