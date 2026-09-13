package net.wady.rendering;

import javafx.scene.paint.Color;
import net.wady.gameobjects.GameObject;
import net.wady.worldmanagement.Tile;
import net.wady.worldmanagement.World;

// THIS CLASS IS THE INTERFACE BETWEEN GAME-LOGIC AND JAVAFX
// THIS IS THE ONLY CLASS THAT SHOULD BE CONVERTING GAME-LOGIC INFO INTO RENDERED DATA.

public class WorldPresenter {

    private final Camera camera;

    public WorldPresenter(Camera camera) {

        this.camera = camera;
    }

    public void sync(World world, TerminalGrid fg, BackgroundGrid bg, int ROWS_perScreen, int COLS_perScreen) {
        for (int row = 0; row < ROWS_perScreen; row++) {
            for (int col = 0; col < COLS_perScreen; col++) {
                Tile tile = world.tileAt(camera.worldX() + col, camera.worldY() + row);
                fg.setGlyph(col, row, tile.terrainType().glyph(), toFxColor(tile.terrainType().fgColor()));
                bg.setBackground(col, row, toFxColor(tile.terrainType().bgColor()));

            }
        }

        for (GameObject go : world.getGameObjectManager().getGameObjects()) {
            RenderInfo r = go.getRenderInfo();
            fg.setGlyph(go.getPosition().getX(), go.getPosition().getY(), r.glyph(), Color.rgb(r.fgR(), r.fgB(), r.fgG(), 1));
        }

    }

    private Color toFxColor(int hexColor) {
        int alpha = (hexColor >> 24) & 0xFF;
        int r = (hexColor >> 16) & 0xFF;
        int g = (hexColor >> 8) & 0xFF;
        int b = hexColor & 0xFF;
        double a = alpha / 255.0;
        Color fxColor = Color.rgb(r, g, b, a);

        return fxColor;
    }


}