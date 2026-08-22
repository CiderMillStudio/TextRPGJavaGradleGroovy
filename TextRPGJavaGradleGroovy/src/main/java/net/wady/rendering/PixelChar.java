package net.wady.rendering;

import java.awt.*;

public class PixelChar {
    private char character;
    private int layer;
    private PixelColor color;


    public PixelChar(char character, int layer, PixelColor color){
        this.character = character;
        this.layer = layer;
        this.color = color;
    }

    public char getCharacter() {
        return character;
    }

    public int getLayer() {
        return layer;
    }

    public String getColorConsole() {

        switch (color) {
            case PixelColor.RED:
                return ColorConsole.ANSI_RED;
            case PixelColor.GREEN:
                return ColorConsole.ANSI_GREEN;

            case PixelColor.BLUE:
                return ColorConsole.ANSI_BLUE;

            case PixelColor.BLACK:
                return ColorConsole.ANSI_BLACK;

            default:
                return ColorConsole.ANSI_WHITE;
        }


    }
}
