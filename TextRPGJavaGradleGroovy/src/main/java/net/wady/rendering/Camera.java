package net.wady.rendering;


import net.wady.physics.Vector;
import net.wady.player.playerevents.PlayerMoveEvent;
import net.wady.player.playerevents.PlayerMoveListener;

public class Camera implements PlayerMoveListener {
    private final int ROWS;
    private final int COLS;
    private int worldX, worldY; // top-left of viewport, in tile/grid coordinates
    private int rightCameraBordersWorldX;
    private int leftCameraBordersWorldX;
    private int topCameraBordersWorldY;
    private int bottomCameraBordersWorldY;

    public Camera(int ROWS, int COLS) {
        this.ROWS = ROWS;
        this.COLS=  COLS;

        resetCameraBorders();


    }

    private void resetCameraBorders() {
        rightCameraBordersWorldX = worldX + COLS - 1;
        leftCameraBordersWorldX = worldX;
        topCameraBordersWorldY = worldY;
        bottomCameraBordersWorldY = worldY + ROWS - 1;

        System.out.println("CAMERA's BORDERS: \nRIGHTX: " + rightCameraBordersWorldX +
                "\nLEFTX: " + leftCameraBordersWorldX +
                "\nTOPY: " + topCameraBordersWorldY +
                "\nBOTTOMY: " + bottomCameraBordersWorldY);
    }

    public void moveCameraToWorldPosition(int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;

        resetCameraBorders();
    }

    public void moveCameraToWorldX(int worldX){
        this.worldX = worldX;

        resetCameraBorders();
    }

    public void moveCameraToWorldY(int worldY){
        this.worldY = worldY;

        resetCameraBorders();
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
            if (Math.abs(worldPosition.x()) % COLS == 0) {
                screenX = 0;
            }
            else {
                screenX = COLS - (Math.abs(worldPosition.x()) % COLS);
            }
        }

        if (worldPosition.y() >= 0) {
            screenY = worldPosition.y() % ROWS;
        }

        else {
            if (Math.abs(worldPosition.y()) % ROWS == 0) {
                screenY = 0;
            }
            else {
                screenY = ROWS - (Math.abs(worldPosition.y()) % ROWS);
            }

            // System.out.println("CAMERA: SCREEN Y = " + screenY);
        }


        return new Vector(screenX, screenY);
    }


    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        System.out.println("CAMERA IS LISTENING!");

        int newWorldX = event.getNewPosition().x();
        int newWorldY = event.getNewPosition().y();

        if (newWorldX > rightCameraBordersWorldX) {
            moveCameraToWorldX(newWorldX); // which also resets Camera's world borders
        }

        if (newWorldX < leftCameraBordersWorldX) {
            moveCameraToWorldX(newWorldX - (COLS - 1));
        }

        if (newWorldY > bottomCameraBordersWorldY) {
            moveCameraToWorldY(newWorldY);
        }

        if (newWorldY < topCameraBordersWorldY) {
            moveCameraToWorldY(newWorldY - (ROWS - 1));
        }

        /*if (event.getNewPosition().x() % COLS == 0 && event.getMoveDirection().x() != 0) {
            if (event.getMoveDirection().x() > 0) { // if we're moving a screen RIGHT, just move the camera's LEFT boundary to the new X position
                moveToWorldX(event.getNewPosition().x());
            }
            else if (event.getMoveDirection().x() < 0) { // if we're moving a screen LEFT, move the camera's LEFT boundary to the new X position - COLS
                moveToWorldX(event.getNewPosition().x() - COLS);
            }
        }

        if (event.getNewPosition().y() % ROWS == 0 && event.getMoveDirection().y() != 0) {
            if (event.getMoveDirection().y() > 0) { // if we're moving a screen DOWN, just move the camera's TOP boundary to the new Y position
                moveToWorldX(event.getNewPosition().y());
            }
            else if (event.getMoveDirection().y() < 0) { // if we're moving a screen UP, move the camera's TOP boundary to the new Y position - ROWS
                moveToWorldX(event.getNewPosition().y() - ROWS);
            }
        }

        if (event.getNewPosition().y() % ROWS == (ROWS-1) && event.getMoveDirection().y() != 0) {
            *//*if (event.getMoveDirection().y() > 0) { // if we're moving a screen DOWN, just move the camera's TOP boundary to the new Y position
                moveToWorldX(event.getNewPosition().y());
            }
            else*//* if (event.getMoveDirection().y() < 0) { // if we're moving a screen UP, move the camera's TOP boundary to the new Y position - (ROWS - 1)
                moveToWorldX(event.getNewPosition().y() - (ROWS-1));
            }
        }*/

        System.out.println("CAMERA'S NEW POSITION IS: " + this.worldX + ", " + this.worldY);


    }
}
