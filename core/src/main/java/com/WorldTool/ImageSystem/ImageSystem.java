package com.WorldTool.ImageSystem;

import java.awt.image.BufferedImage;

public class ImageSystem {

    private String path;

    private SavePNG save;
    private LoadPNG load;
    private ImageConverter imageConverter;

    public ImageSystem(String path) {
        // Ensure the path ends with a separator, then append "textures"
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += System.getProperty("file.separator");
        }
        this.path = path + "textures";

        this.save = new SavePNG();
        this.load = new LoadPNG();
        this.imageConverter = new ImageConverter();
    }

    public void Save(int id, BufferedImage image) {
        save.PNG(path, id, image);
    }

    public BufferedImage Load(int id) {
        return load.PNG(path, id);
    }

    public int[][] toARGBArray(BufferedImage input) {
        return imageConverter.toARGBArray(input);
    }

    public BufferedImage fromARGBArray(int[][] input) {
        return imageConverter.fromARGBArray(input);
    }
}
