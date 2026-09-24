package net.wady.worldmanagement;

public enum TerrainType {
    /*FLOOR           (true, 1, false, '.', *//*fg*//* 0xFF909090, *//*bg*//* 0x00000000),
    WALL            (false, 0, true, '#', 0xFFFFFFFF, 0x00000000),
    WATER_SHALLOW   (true, 2, false, '~', 0xFF90D6FF, 0x00000000),
    WATER_DEEP      (false, 0, false, '\u2248', 0xFF111184, 0x00000000),
    LAVA            (false, 0, false, '\u2248', 0xFFFF991C, 0x00000000);*/

    FLOOR           (true, 1, false, '\u2248', /*fg*/ 0xFF006994, /*bg*/ 0x00000000), // water
    WALL            (false, 0, true, '.', 0xFFF2D779, 0x00000000), //beach
    WATER_SHALLOW   (true, 2, false, '\u2660', 0xFF486B00, 0x00000000), // forest
    WATER_DEEP      (false, 0, false, '#', 0xFF606060, 0x00000000),         // stone
    LAVA            (false, 0, false, '\u25B2', 0xFFFFFFFF, 0x00000000);    // peak

    final boolean passable;
    final int moveCost;
    final boolean blocksLOS;
    final char glyph;
    final int fgColor;
    final int bgColor;

    TerrainType(boolean passable, int moveCost, boolean blocksLOS, char glyph, int fgColor, int bgColor) {
        this.passable = passable;
        this.moveCost = moveCost;
        this.blocksLOS = blocksLOS;
        this.glyph = glyph;
        this.fgColor = fgColor;
        this.bgColor = bgColor;
    }

    public int bgColor() {
        return bgColor;
    }

    public int fgColor() {
        return fgColor;
    }

    public char glyph() {
        return glyph;
    }

    public boolean blocksLOS() {
        return blocksLOS;
    }

    public int moveCost() {
        return moveCost;
    }

    public boolean passable() {
        return passable;
    }






}
