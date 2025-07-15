package com.WorldTool.ImageSystem;

import java.awt.image.BufferedImage;

public class ImageConverter {

    // Converts BufferedImage to a 2D array of ARGB values
    public int[][] toARGBArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] argbArray = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                argbArray[y][x] = image.getRGB(x, y); // ARGB packed int
            }
        }
        return argbArray;
    }

    // Converts a 2D array of ARGB values back into a BufferedImage
    public BufferedImage fromARGBArray(int[][] argbArray) {
        int height = argbArray.length;
        int width = argbArray[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, argbArray[y][x]);
            }
        }
        return image;
    }
}
