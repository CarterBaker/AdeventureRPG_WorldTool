package com.WorldTool.ImageSystem;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class LoadPNG {
    public BufferedImage PNG(String path, int id) {
        try {
            File inputFile = new File(path + "/images/" + id + ".png");
            if (!inputFile.exists()) {
                System.err.println("Image not found: " + inputFile.getAbsolutePath());
                return null;
            }

            return ImageIO.read(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
