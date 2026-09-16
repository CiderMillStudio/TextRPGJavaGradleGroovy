package net.wady.rendering;


import net.wady.physics.Vector;
import net.wady.player.playerevents.PlayerMoveEvent;
import net.wady.player.playerevents.PlayerMoveListener;

public class Camera implements PlayerMoveListener {
    private int worldX, worldY; // top-left of viewport, in tile/grid coordinates
    private final int ROWS;
    private final int COLS;

    public Camera(int ROWS, int COLS) {
        this.ROWS = ROWS;
        this.COLS=  COLS;
    }

    public void moveToWorldPosition(int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public void moveToWorldX(int worldX){
        this.worldX = worldX;
    }

    public void moveToWorldY(int worldY){
        this.worldY = worldY;
    }

    public void center (int entityX, int entityY, int viewCols, int viewRows) {
        this.worldX = entityX - (viewCols / 2);
        this.worldY = entityY - (viewRows / 2);
    }

    public int worldX() {
        return worldX;
    }

    public int worldY() {
        return worldY;
    }

    public Vector convertWorldPositionToScreenPosition(Vector worldPosition) {


        int screenX = 0;
        int screenY = 0;

        if (worldPosition.x() >= 0) {
            screenX = worldPosition.x() % COLS;
        }
        else {
            screenX = COLS - Math.abs(worldPosition.x() % COLS);
        }

        if (worldPosition.y() >= 0) {
            screenY = worldPosition.y() % ROWS;
        }
        else {
            screenY = ROWS - Math.abs(worldPosition.y() % ROWS);
        }


        return new Vector(screenX, screenY);
    }


    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        System.out.println("CAMERA IS LISTENING!");

        if (event.getNewPosition().x() % COLS == 0) {
            moveToWorldX(event.getNewPosition().x());
        }

        if (event.getNewPosition().y() % ROWS == 0) {
            moveToWorldY(event.getNewPosition().y());
        }

        System.out.println("CAMERA'S NEW POSITION IS: " + this.worldX + ", " + this.worldY);


    }
}
