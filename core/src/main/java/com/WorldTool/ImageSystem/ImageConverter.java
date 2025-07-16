package com.WorldTool.ImageSystem;

import java.awt.image.BufferedImage;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

    public TextureRegion convertToTextureRegion(int[][] argbData) {
        int height = argbData.length;
        int width = argbData[0].length;

        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = argbData[y][x];
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = argb & 0xff;

                int rgba = (r << 24) | (g << 16) | (b << 8) | a;
                pixmap.drawPixel(x, y, rgba);
            }
        }

        Texture texture = new Texture(pixmap);
        TextureRegion region = new TextureRegion(texture);
        pixmap.dispose();

        return region;
    }

}
