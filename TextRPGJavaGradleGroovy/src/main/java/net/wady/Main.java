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
import net.wady.rendering.BackgroundGrid;
import net.wady.rendering.TerminalGrid;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    private static final int COLS = 90;
    private static final int ROWS = 55;
    private static final int SCALE = 3; // must be an integer (2 -> 16x16 pixel cells)

    private BackgroundGrid bgGrid;
    private TerminalGrid fgGrid;
    private int tick = 0;


    @Override
    public void start(Stage stage) throws Exception {
        Canvas fgCanvas = new Canvas(COLS * 8 * SCALE, ROWS * 8 * SCALE);
        Canvas bgCanvas = new Canvas(COLS * 8 * SCALE, ROWS * 8 * SCALE);

        GraphicsContext gc = fgCanvas.getGraphicsContext2D();

        bgGrid = new BackgroundGrid(bgCanvas.getGraphicsContext2D(), COLS, ROWS, SCALE);
        fgGrid = new TerminalGrid(loadAtlas(), gc, COLS, ROWS, SCALE);
        fgGrid.clear(Color.BLACK);

        drawBorder(Color.GRAY);
        drawText(2, 2, "ANSI ROGUELIKE - Bm437 Acer VGA 8x8", Color.web("#55FF55"));
        drawText(2, 4, "Extracted straight from the EBDT bitmap strikes", Color.web("#AAAAAA"));
        drawText(2, 6, "Box drawing: \u2554\u2550\u2550\u2557 \u2551  \u2551 \u255A\u2550\u2550\u255D", Color.web("#5555FF"));
        drawText(2, 8, "Card suits: \u2660 \u2665 \u2666 \u2663", Color.web("#FF5555"));
        drawText(2, 10, "House symbol: \u2302", Color.SEAGREEN);

        StackPane root = new StackPane(bgCanvas, fgCanvas);
        Scene scene = new Scene(root, Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("TerminalGrid demo");
        stage.setResizable(false);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // prove the loop runs every frame: an animated @ bouncing on row 12
                tick++;
                int x = 2 + (tick / 50) % 70;
                fgGrid.setGlyph(x, 12,'@', Color.YELLOW);
                bgGrid.setBackground(x, 12, Color.RED);
                if (x > 2) {
                    fgGrid.setGlyph(x - 1, 12, ' ', Color.WHITE);
                    bgGrid.setBackground(x - 1, 12, Color.BLACK);
                }




                bgGrid.render();
                fgGrid.render();
            }
        }.start();



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

        // Open Start Menu
        /*
         *
         * Open the game's start screen (Published by: x, CiderMillStudio)
         * Open the start menu, play starting music
         * Show Title Screen, allowing player to choose from one of 4 save files.
         * Save files should either be empty (showing "Start New Game"), or should input the save file info (Character Name, time spent playing, player location, etc...)
         * Upon selection, the selected gamefile should be initialized.
         *
         *
         * */



        // Initialize game
        /*
         * - Collect file data to figure out what exactly needs to be loaded into the game via the game_init_data.json
         * - If new world, procedurally generate set of DUNGEONS, each with between 1-20 floors. (BSP Trees)
         * - If new world, procedurally generate an overworld. (Drunkards Walk)
         *
         * */

       /* ObjectMapper mapper = new ObjectMapper();
        GameData gameData = new GameData();


        try (InputStream is = Main.class.getResourceAsStream("/game_init_data.json")) {
            if (is == null) {
                throw new FileNotFoundException("game_init_data.json not found on classpath '/'");
            }
            gameData = mapper.readValue(is, GameData.class);
        }


        String json = mapper.writeValueAsString(gameData);

        System.out.println("loading gamedata from the following json fields: \n" + json);


        // --- GAME LOOP ------

        GameLoop gameLoop = new GameLoop();

        // Starts the loop -- this call returns almost immediately, since
        // scheduleAtFixedRate() runs on its own background thread.
        gameLoop.start();

        try {
            gameLoop.awaitCompletion();
        } catch (InterruptedException e) {
            // If something interrupts main() itself while waiting,
            // restore the interrupt flag and bail out gracefully.
            Thread.currentThread().interrupt();
            System.err.println("Game loop was interrupted before completion.");
        }

        // --- Execution only reaches this point after the loop has truly ended ---



        *//* Terminate Game
         *
         * Decide whether or not to save game
         * If yes, save game and exit the game
         *
         * *//*

        File saveFile = new File(System.getProperty("user.dir"), ".textrpg/gamedata.json");
        saveFile.getParentFile().mkdirs();

        mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, gameData);

        json = mapper.writeValueAsString(gameData);
        System.out.println("saving gamedata to JSON with following info: \n" + json);*/



    }



}
