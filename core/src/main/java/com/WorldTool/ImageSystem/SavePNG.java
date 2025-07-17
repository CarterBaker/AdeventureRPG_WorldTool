package com.WorldTool.ImageSystem;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class SavePNG {
    public void PNG(String path, int id, BufferedImage image) {
        try {
            File outputDir = new File(path);
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Ensure the images/ directory exists
            }

            File outputFile = new File(outputDir, id + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
