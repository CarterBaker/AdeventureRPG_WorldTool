package com.WorldTool.ImageSystem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class LoadPNG {
    public BufferedImage PNG(String path, int id) {
        try {
            File inputFile = new File(path + id + ".png");

            if (!inputFile.exists()) {
                System.err.println("Image not found: " + inputFile.getAbsolutePath());

                // Create directories if needed
                inputFile.getParentFile().mkdirs();

                // Create a blank white image
                BufferedImage whiteImage = createBlankWhiteImage(16, 16);

                // Save the white image to disk
                ImageIO.write(whiteImage, "png", inputFile);
                System.out.println("White image created at: " + inputFile.getAbsolutePath());

                return whiteImage;
            }

            return ImageIO.read(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
            return createBlankWhiteImage(16, 16); // Fallback white image
        }
    }

    private BufferedImage createBlankWhiteImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE); // Fill with white
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return image;
    }
}
