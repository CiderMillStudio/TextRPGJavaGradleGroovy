package net.wady.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BackgroundGrid {

    private final GraphicsContext gc;
    private final int cols, rows;
    private final int cellW, cellH;

    private final Color[] bg;
    private final boolean[] dirty;
    private boolean allDirty = true;

    public BackgroundGrid(GraphicsContext gc, int cols, int rows, int scale) {
        this.gc = gc;
        this.cols = cols;
        this.rows = rows;
        this.cellW = 8 * scale;
        this.cellH = 8 * scale;

        int n = cols * rows;
        this.bg = new Color[n];
        this.dirty = new boolean[n];
        Color defaultBg = Color.BLACK;

        for (int i = 0; i < n; i++) {
            bg[i] = defaultBg;
        }
    }

    public void setBackground(int col, int row, Color color) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) return;

        int i = row * cols + col;

        if (bg[i].equals(color)) {
            return;
        }

        bg[i] = color;
        dirty[i] = true;
    }

    public void clear(Color color) {
        int n = cols * rows;
        for (int i = 0; i < n; i++) bg[i] = color;
        allDirty = true;
    }

    public void render() {
        // Paint one solid full-canvas rect on any frame where everything is dirty (startup, clear()). This matters on
        // Fractional-DPI displays: adjacent per-cell fillRect calls can round to physical pixels slightly differently
        // and leave 1px seams between cells otherwise.
        if (allDirty) {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, cellW * cols, cellH * rows);
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int i = row * cols + col;
                if (!allDirty && !dirty[i]) continue;

                double x = col * cellW;
                double y = row * cellH;
                gc.setFill(bg[i]);
                gc.fillRect(x, y, cellW, cellH);
                dirty[i] = false;
            }
        }

        allDirty = false;
    }

}
