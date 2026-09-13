package net.wady.worldgeneration;


import de.articdive.jnoise.pipeline.JNoise;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NoiseImageSaver {

    public void savePixelatedNoise(JNoise noisePipeline, String outputPath, int width, int height, int pixelSize) {
        // Create an image to hold the final visual output
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y += pixelSize) {
            for (int x = 0; x < width; x += pixelSize) {

                // Sample the noise at the coordinates (scaled down to keep the noise pattern coherent)
                double sampleX = (double) x / width * 10.0;
                double sampleY = (double) y / height * 10.0;

                // Evaluate the noise pipeline (JNoise outputs are generally between -1.0 and 1.0)
                double noiseValue = noisePipeline.evaluateNoise(sampleX, sampleY);

                // Normalize the noise from [-1.0, 1.0] to [0.0, 1.0]
                double normalizedValue = (noiseValue + 1.0) / 2.0;
                normalizedValue = Math.max(0.0, Math.min(1.0, normalizedValue)); // Clamp bounds

                // Convert to a grayscale intensity (0-255)
                int grayscale = (int) (normalizedValue * 255);
                grayscale = grayscaleCondensor(grayscale);
                Color color = new Color(grayscale, grayscale, grayscale);

                // Fill a pixel block (creates the "pixelated" chunk effect)
                for (int blockY = 0; blockY < pixelSize && (y + blockY) < height; blockY++) {
                    for (int blockX = 0; blockX < pixelSize && (x + blockX) < width; blockX++) {
                        image.setRGB(x + blockX, y + blockY, color.getRGB());
                    }
                }
            }
        }

        // Export the resulting image to disk
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Pixelated noise map saved successfully to " + outputPath);
        } catch (IOException e) {
            System.err.println("Error saving the noise image: " + e.getMessage());
        }
    }

    private int grayscaleCondensor(int grayscaleValue) {

        int comparatorValue = 0;
        if (grayscaleValue >= 230) comparatorValue = 4;
        else if (grayscaleValue >= 200) comparatorValue = 3; // WHITE GRAY (mtns?)
        else if (grayscaleValue >= 140) comparatorValue = 2; // LIGHT GRAY (forest?)
        else if (grayscaleValue >= 100) comparatorValue = 1; // DARK GRAY (water?)
        else comparatorValue = 0; // BLACK (Deep water?)

        switch (comparatorValue) {
            case (0):
                return 0;

            case (1):
                return 63;

            case (2):
                return 127;

            case (3):
                return 191;

            case (4):
                return 255;

            default:
                return 255;

        }
    }

}
