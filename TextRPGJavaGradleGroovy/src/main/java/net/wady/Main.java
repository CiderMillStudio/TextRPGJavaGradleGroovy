package net.wady;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


// import JavaFX classes:
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.wady.gameengine.KeyInputListener;
import net.wady.rendering.BackgroundGrid;
import net.wady.rendering.Camera;
import net.wady.rendering.TerminalGrid;
import net.wady.rendering.WorldPresenter;
import net.wady.worldgeneration.NoiseGenerator;
import net.wady.worldgeneration.NoiseImageSaver;
import net.wady.worldmanagement.World;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    private static final int COLS = 60;
    private static final int ROWS = 35;
    private static final int SCALE = 3; // must be an integer (2 -> 16x16 pixel cells)
    private static final int TICKS_PER_SECOND = 20; // 20 frames per second (max)

    // store currently held-down keys:
    // private final KeyInputListener keyInputListener = new KeyInputListener();

    private BackgroundGrid bgGrid;
    private TerminalGrid fgGrid;
    private long currentTick = 0;

    private static final long NS_PER_TICK = 1_000_000_000L / TICKS_PER_SECOND;
    private long lastTime = -1;
    private long accumulator = 0;


    // NEED TO MAKE THIS PRETTIER, this is just for testing purposes:
    // private Player player = new Player();
    private long testLong = 0l;
    private final World world = new World(testLong);
    private final Camera camera = new Camera(ROWS, COLS);
    private final WorldPresenter worldPresenter = new WorldPresenter(camera);


    @Override
    public void start(Stage stage) throws Exception {
        Canvas bgCanvas = new Canvas(COLS * 8 * SCALE, ROWS * 8 * SCALE);
        Canvas fgCanvas = new Canvas(COLS * 8 * SCALE, ROWS * 8 * SCALE);

        GraphicsContext bgGraphicsContext = bgCanvas.getGraphicsContext2D();
        GraphicsContext fgGraphicContext = fgCanvas.getGraphicsContext2D();

        bgGrid = new BackgroundGrid(bgGraphicsContext, COLS, ROWS, SCALE);
        fgGrid = new TerminalGrid(loadAtlas(), fgGraphicContext, COLS, ROWS, SCALE);
        fgGrid.clear(Color.BLACK);

        /*drawBorder(Color.GRAY);
        drawText(2, 2, "ANSI ROGUELIKE - Bm437 Acer VGA 8x8", Color.web("#55FF55"));
        drawText(2, 4, "Extracted straight from the EBDT bitmap strikes", Color.web("#AAAAAA"));
        drawText(2, 6, "Box drawing: \u2554\u2550\u2550\u2557 \u2551  \u2551 \u255A\u2550\u2550\u255D", Color.web("#5555FF"));
        drawText(2, 8, "Card suits: \u2660 \u2665 \u2666 \u2663", Color.web("#FF5555"));
        drawText(2, 10, "House symbol: \u2302", Color.SEAGREEN);*/

        StackPane root = new StackPane(bgCanvas, fgCanvas);
        Scene scene = new Scene(root, Color.BLACK);

        // listen for key events
        scene.setOnKeyPressed(e -> KeyInputListener.getInstance().keyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> KeyInputListener.getInstance().keyReleased(e.getCode()));

        world.SpawnPlayer();
        world.player.addMoveListener(worldPresenter.camera);

        stage.setScene(scene);
        stage.setTitle("Roguelike Prototype");
        stage.setResizable(false);
        stage.show();



        // The ANimationTimer is JavaFX's way of updating the screen
        new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (lastTime < 0) {
                    lastTime = now; // first call: nothing to compare against yet.
                    return;
                }

                // System.out.println(currentTick);

                long elapsed = now - lastTime;
                lastTime = now;
                accumulator += elapsed;


                // Guard against a huge elapsed value after, for example, a debugger pause or the window losing focus.
                // without this, a single stall could queue up hundreds of ticks that all ifre back-to-back trying to
                // "catch up" / stutter.
                if (accumulator > NS_PER_TICK * 5) {
                    accumulator = NS_PER_TICK * 5;
                }

                while (accumulator >= NS_PER_TICK /*currently going at 20 ticks per second*/) {
                    tick(NS_PER_TICK); // note that number of NANO-seconds are being passed to tick as deltaTime.
                    currentTick++;
                    accumulator -= NS_PER_TICK;
                }


                // Note: always render the background grid FIRST before rendering the foreground grid
                bgGrid.render();
                fgGrid.render();
            }
        }.start();


    }

    public void tick(long deltaTime) {

        world.worldTick(deltaTime);
        worldPresenter.sync(world, fgGrid, bgGrid, ROWS, COLS);
        // drawBorder(Color.GRAY);

    }

    public void drawBorder(Color borderFgColor) {

        for (int c = 0; c < COLS; c++) {
            fgGrid.setGlyph(c, 0, '\u2550', borderFgColor);
            fgGrid.setGlyph(c, ROWS - 1, '\u2550', borderFgColor);
        }
        for (int r = 0; r < ROWS; r++) {
            fgGrid.setGlyph(0, r, '\u2551', borderFgColor);
            fgGrid.setGlyph(COLS - 1, r, '\u2551', borderFgColor);
        }

        fgGrid.setGlyph(0, 0, '\u2554', borderFgColor);
        fgGrid.setGlyph(COLS - 1, 0, '\u2557', borderFgColor);
        fgGrid.setGlyph(0, ROWS - 1,'\u255A', borderFgColor);
        fgGrid.setGlyph(COLS - 1, ROWS - 1, '\u255D', borderFgColor);

    }

    private void drawText(int col, int row, String s, Color color) {
        for (int i = 0; i < s.length(); i++) {
            fgGrid.setGlyph(col + i, row, s.charAt(i), color);
            System.out.println(s.charAt(i));
        }
    }

    private InputStream loadAtlas() throws Exception {
        return Main.class.getResourceAsStream("/images/fonts/cp437_8x8_atlas.png");
    }

    public static void main(String[] args) throws IOException {

        launch(args); // an inherited class from Application (JavaFX)
        NoiseGenerator noise = new NoiseGenerator(333329614l, 4, 0.25, 0.5f, 1f);


        NoiseImageSaver noiseImageSaver = new NoiseImageSaver();

        noiseImageSaver.savePixelatedNoise(noise.getJNoise(), "pixelated_noise4.png", 32, 32, 2);


    }



}
