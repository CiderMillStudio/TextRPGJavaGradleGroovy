package net.wady;

import net.wady.data.GameData;
import net.wady.gameengine.GameLoop;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {


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



        // Terminate Game
        /*
         *
         * Decide whether or not to save game
         * If yes, save game and exit the game
         *
         *
         * */

        File saveFile = new File(System.getProperty("user.home"), ".textrpg/gamedata.json");
        saveFile.getParentFile().mkdirs();

        mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, gameData);

        json = mapper.writeValueAsString(gameData);
        System.out.println("saving gamedata to JSON with following info: \n" + json);



    }









}
