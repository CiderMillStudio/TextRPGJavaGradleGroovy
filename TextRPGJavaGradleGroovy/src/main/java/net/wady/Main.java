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
import net.wady.worldmanagement.World;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    private static final int COLS = 60;
    private static final int ROWS = 35;
    private static final int SCALE = 3; // must be an integer (2 -> 16x16 pixel cells)
    private static final int TICKS_PER_SECOND = 40; // 40 frames per second (max)

    // store currently held-down keys:
    // private final KeyInputListener keyInputListener = new KeyInputListener();

    private BackgroundGrid bgGrid;
    private TerminalGrid fgGrid;
    private long currentTick = 0;

    private static final long NS_PER_TICK = 1_000_000_000L / TICKS_PER_SECOND;
    private long lastTime = -1;
    private long accumulator = 0;


    // World Generation
    // private final NoiseGenerator noiseGenerator = new NoiseGenerator();


    // NEED TO MAKE THIS PRETTIER, this is just for testing purposes:
    // private Player player = new Player();
    private long testWorldSeed = 4320l;
    private final World world = new World(testWorldSeed);
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
            // System.out.println(s.charAt(i));
        }
    }

    private InputStream loadAtlas() throws Exception {
        return Main.class.getResourceAsStream("/images/fonts/cp437_8x8_atlas.png");
    }


    public static void main(String[] args) throws IOException {

        launch(args); // an inherited class from Application (JavaFX)

        /*NoiseGenerator noiseGenerator = new NoiseGenerator(this.testWorldSeed);
        noiseGenerator.makeTestNoise(339775653333314l, 4, 3, 0.13f, 1.1f);*/


    }



}
