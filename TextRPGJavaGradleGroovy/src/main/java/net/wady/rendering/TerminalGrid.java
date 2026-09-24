package net.wady.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/* This Terminal Grid renders a grid of CP437 glyphs from a hand-built texture atlas,
of relying on JavaFX's text and font pipeline, which doesn't work well with BitMap Fonts
(and we get to learn new things along the way:) )

The font that I'm using for now (Bm437_Acer_VGA_8x8.otb) stores glyphs as embedded OpenType
bitmap strikes (EBDT/EBLC), not vector outlines. JavaFX has a text shaper that rasterizes
from outline data, so loading the bitmap font directly via Font.loadFont() is unreliable.
Blitting glyphs from an atlasonto a canvas gives better control over pixel alignment and
crispness (this is what Dwarf Fortress does).

The atlas (cp437_8x8_atlas.png in /resources/images/fonts/cp437_8x8_atlas.png) is a
128x128 image: 16 columns x 16 rows of 8x8 glyphs, one per CP437 code point 0-255, laid out
in the same order as the classic PC-8 codepage. Glyph pixels are opaque white, and everything
else is transparent (i.e. alpha = 0). That lets you tint each glyph to any foregrand color
per cell using an SRC_ATOP blend cheaply.

 */
public class TerminalGrid {

    public static final int GLYPH_W = 8;
    public static final int GLYPH_H = 8;
    public static final int ATLAS_COLS = 16;
    public static final int ATLAS_ROWS = 16;

    // Now, we'll do REVERSE CP437 lookup: unicode char -> code point index (0-255)
    // Built once from the JDK's own IBM437 charset, so it will always match
    // the atlas's layout exactly.

    private static final Map<Character, Integer> CP437_INDEX = buildCp437Index();

    private static Map<Character, Integer> buildCp437Index() {
        Charset cp437 = Charset.forName("IBM437"); // aliases: cp437, 437
        byte[] raw = new byte[256];

        for (int i = 0; i < 256; i++) {
            raw[i] = (byte) i;
        }

        String decoded = new String(raw, cp437);
        // System.out.println(decoded);

        Map<Character, Integer> map = new HashMap<>(512); // we make the initial capacity 512 as a
        // performance nicety. (a hashmap resizes/rehashes everything once it gets too full relative to its internal
        // array size, so giving it double the entry count as an initial capacity means it never has to resize while
        // filling up.

        for (int i = 0; i < 256; i++) {
            map.put(decoded.charAt(i), i);
        }

        // DOS text mode displayed control bytes 0x00-0x1F and 0x7F using a
        // fixed set of glyphs (smileys, suits, arrows...) instead of their
        // literal control-character meaning. IBM437 decodes those bytes to
        // the literal control chars (U+0000-U+001F, U+007F), so the "nice"
        // Unicode symbols people actually expect to type (e.g. '\u2665' for
        // heart) have no entry yet. Alias them onto the same indices here.
        char[] controlPictures = {
                '\u263A','\u263B','\u2665','\u2666','\u2663','\u2660','\u2022','\u25D8', // 1-8
                '\u25CB','\u25D9','\u2642','\u2640','\u266A','\u266B','\u263C',          // 9-15
                '\u25BA','\u25C4','\u2195','\u203C','\u00B6','\u00A7','\u25AC','\u21A8', // 16-23
                '\u2191','\u2193','\u2192','\u2190','\u221F','\u2194','\u25B2','\u25BC'  // 24-31
        };
        for (int i = 0; i < controlPictures.length; i++) {
            map.put(controlPictures[i], i + 1); // index 1..31
        }

        map.put('\u2302', 127); // DEL -> house symbol (⌂)

        return map;
    }

    private final Image atlas;
    private final GraphicsContext gc;
    private final int cols, rows;
    private final int cellW, cellH; // on-screen cell size in device-independent pixels

    // Backing buffers, one entry per cell (row-major).
    private final int[] glyphIndex;  // CP437 index 0-255
    private final Color[] fg;
    private final boolean[] dirty;
    private boolean allDirty = true;


    /*
    @param atlastStream     -> Stream for pc437_8x8_atlas.png (e.g. from getResourceAsStream)
    @param gc               -> the GraphicsContext of the Canvas you're drawing into
    @param cols             -> grid width in glyphs
    @param rows             -> grid height in glyphs
    @param scale            -> integer scale factor applied to the native 8x8 glyph size.
                               Use an INTEGER to keep glyphs pixel-perfect.
                               Non-integer scaling will smea/uneven the 1-bit glyph edges
     */

    public TerminalGrid(InputStream atlasStream, GraphicsContext gc, int cols, int rows, int scale) {
        if (scale < 1) throw new IllegalArgumentException("scale must be >= 1 (and must be an INTEGER)");

        this.atlas = new Image (atlasStream);
        int expectedW = ATLAS_COLS * GLYPH_W;
        int expectedH = ATLAS_ROWS * GLYPH_H;
        if ((int) atlas.getWidth() != expectedW || (int) atlas.getHeight() != expectedH) {
            throw new IllegalArgumentException(
                    "Atlas image is " + (int) atlas.getWidth() + "x" + (int) atlas.getHeight()
                    + " but expected " + expectedW + "x" + expectedH
                    + " (16 cols x 16 rows of 8x8 glyphs... -- wrong file?"
            );
        }

        this.gc = gc;
        this.cols = cols;
        this.rows = rows;
        this.cellW = GLYPH_W * scale;
        this.cellH = GLYPH_H * scale;

        int n = cols * rows; // (number of glyphs on screen
        this.glyphIndex = new int[n];
        this.fg = new Color[n];
        this.dirty = new boolean[n];

        Color defaultFg = Color.WHITE;

        for (int i = 0; i < n; i++) {
            glyphIndex[i] = 0; // NUL glyph, effectively blank
            fg[i] = defaultFg;
        }

    }

    public int getPixelWidth() { return cols * cellW; } // returns width of screen in pixels
    public int getPixelHeight() { return rows * cellH; } // returns height of screen in pixels

    /* Set a single cell's character and colors. Marks the cell dirty for the next render(). */
    public void setGlyph(int col, int row, char c, Color foregroundColor) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) return;
        Integer idx = CP437_INDEX.get(c);
        int cp437Index = (idx != null) ? idx : 0x3F; // fall back to '?' if not in CP437
        int i = row * cols + col;

        if (glyphIndex[i] == cp437Index && fg[i].equals(foregroundColor)) {
            return;
        }

        glyphIndex[i] = cp437Index;
        fg[i] = foregroundColor;
        dirty[i] = true;
    }


    /* Convenience overload keeping current colors, changing only the character. */
    public void setChar(int col, int row, char c) {
        int i = row * cols + col;
        setGlyph(col, row, c, fg[i]);
    }

    public void clear(Color backgroundColor) {
        int n = cols * rows;
        for (int i = 0; i < n; i++ ) {
            glyphIndex[i] = 0;
        }

        allDirty = true;
    }


    /* Redraw the grid. Call once per frame (e.g. from Animation Timer).
    * Only cells changed since last render are actually redrawn,
    * which matters since we're pushing 4000 (80x50) cells at 60fps. */
    public void render() {
        gc.setImageSmoothing(false); // this is critical: no bilinear blur when scaling 8x8 --> cellW * cellH

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int i = row * cols + col;
                // we can skip this cell entirely if it hasn't been changed at all since last frame
                if (!allDirty && !dirty[i]) continue;

                // Snap to integer device pixels so glyph edges never straddle a pixel boundary
                double x = col * cellW;
                double y = row * cellH;

                // 0) reset the cell to fully transparent -- so the alpha mask we build below
                // reflects THIS glyph's shape, not whatever was opaque here on a previous
                // frame.
                gc.clearRect(x, y, cellW, cellH);


                // 1) draw the glyph, scaled with nearest-neighbor (smoothing is already off). (Default is SRC_OVER).
                // Ink pixels become opaque-white, everything else stays fully transparent (alpha 0)
                int gi = glyphIndex[i];
                double sx = (gi % ATLAS_COLS) * GLYPH_W; // sx finds the x coordinate of the pixel within the ATLAS file that is the top-left corner of the glyph which we'll snatch from the atlas file and paste onto the cell.
                double sy = (gi / ATLAS_COLS) * GLYPH_H; // sy is the same thing but for the y-pixel
                gc.drawImage(atlas, sx, sy, GLYPH_W, GLYPH_H, x, y, cellW, cellH);


                // 2) Tint: SRC_ATOP now correctly limits itself to the ink pixels,
                // because those are the only pixels with any alpha right now.
                gc.setGlobalBlendMode(BlendMode.SRC_ATOP);
                gc.setFill(fg[i]);
                gc.fillRect(x, y, cellW, cellH);


                // 3) reset for next cell:
                gc.setGlobalBlendMode(BlendMode.SRC_OVER);

                dirty[i] = false;

            }
        }

        allDirty = false;

    }







}
