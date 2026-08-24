package net.wady.rendering;

import net.wady.gameobjects.Component;
import net.wady.physics.Vector;

public class Sprite implements Component {

    // Sprite.size: dimensions (width, height) of a sprite
    private Vector size;

    // Sprite.pixels: defines which PixelChar's (including spaces, which may have a layer value of -1) fill up the sprite's dimensions
    private PixelChar[] pixels;

    public Sprite(Vector size, PixelChar[] pixels) {
        this.size = size;
        this.pixels = pixels;
    }

    public Sprite(PixelChar pixel) {
        this.size = new Vector(1,1);
        this.pixels = new PixelChar[] {pixel};
    }


}
