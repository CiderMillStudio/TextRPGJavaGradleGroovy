package net.wady.rendering;

import net.wady.physics.Vector;

public class Sprite {
    private Vector size;
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
