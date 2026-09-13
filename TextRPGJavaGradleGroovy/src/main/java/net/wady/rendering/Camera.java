package net.wady.rendering;


public class Camera {
    private int worldX, worldY; // top-left of viewport, in tile/grid coordinates

    public void moveTo(int x, int y) {
        this.worldX = x;
        this.worldY = y;
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


}
