package net.wady;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.wady.data.GameData;
import net.wady.gameengine.GameLoop;


import com.fasterxml.jackson.databind.ObjectMapper;

// import JavaFX classes:
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {



    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/scene.fxml"));

        Group root = new Group();

        Scene scene = new Scene(root, Color.BLACK);
        //scene.getStylesheets().add(getClass().getResource("/org.openjfx/styles.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/images/app/icon.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Text RPG v0.0");
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setResizable(false);
        //stage.setFullScreen(true);
        //stage.setFullScreenExitHint("Press 'q' to escape");
        //stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));

        Text text = new Text();
        text.setText("A quick brown fox jumps \nover the lazy dog.");
        text.setFill(Color.LIGHTSEAGREEN);
        text.setX(600);
        text.setY(400);
        text.setFont(Font.font("Verdana", 50));

        root.getChildren().add(text);





        // can also create a scene with dimensions and color!
        // stage.setScene(scene, 600, 600, Color.LIGHTSKYBLUE)
        stage.setScene(scene);
        stage.show();


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

        ObjectMapper mapper = new ObjectMapper();
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



        /* Terminate Game
         *
         * Decide whether or not to save game
         * If yes, save game and exit the game
         *
         * */

        File saveFile = new File(System.getProperty("user.dir"), ".textrpg/gamedata.json");
        saveFile.getParentFile().mkdirs();

        mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, gameData);

        json = mapper.writeValueAsString(gameData);
        System.out.println("saving gamedata to JSON with following info: \n" + json);



    }



}
