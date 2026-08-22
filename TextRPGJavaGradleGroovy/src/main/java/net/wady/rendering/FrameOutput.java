package net.wady.rendering;

import java.util.ArrayList;
import java.util.List;

public class FrameOutput {

    // This Class is in charge of displaying a 90x30 frame to the screen. Each time a frame fires, it's THIS class that renders it.

    private int height;
    private int width;
    private List<PixelChar> pixels;

    public FrameOutput(int height, int width, List<PixelChar> pixels) {

        this.height = height;
        this.width = width;
        this.pixels = pixels;

    }



    public void printFrame() {
        clearScreen();
        int row = 1;
        int column = 1;

        int currentPixel = 0;

        String lineBuilder = "";


        while (row <= height) {

            column = 1;

            while (column <= width) {

                lineBuilder += pixels.get(currentPixel).getColorConsole();
                lineBuilder += pixels.get(currentPixel).getCharacter();
                lineBuilder += ColorConsole.ANSI_RESET;

                column++;
                currentPixel++;

            }

            // System.out.println("Printing row number = " + row + " which contains " + (column - 1) + " characters.");

            lineBuilder += "\n";
            row++;


        }

        System.out.println(lineBuilder);

        //System.out.println("Whole screen should have printed now.");


    }

    public static void clearScreen() {
        // \033[H moves the cursor to the top left; \033[2J clears the screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }







}
