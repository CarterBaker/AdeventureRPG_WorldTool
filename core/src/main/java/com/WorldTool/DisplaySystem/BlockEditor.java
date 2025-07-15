package com.WorldTool.DisplaySystem;

import com.WorldTool.DisplaySystem.EditorTools.SimpleCubeRenderer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.awt.image.BufferedImage;

public class BlockEditor implements Editor {

    private SimpleCubeRenderer cube;
    private Vector2 cubePosition = new Vector2(250, 75);

    private int topID = 0;
    private int sideID = 0;
    private int bottomID = 0;

    private int[][] topTex;
    private int[][] sideTex;
    private int[][] bottomTex;

    private final DisplaySystem displaySystem;

    public BlockEditor(DisplaySystem input) {
        this.displaySystem = input;
        this.cube = new SimpleCubeRenderer(cubePosition);

        SetTextureIds(0, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        cube.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        // Optional: handle resize
    }

    private void SetTextureIds(int top, int side, int bottom) {
        topID = top;
        sideID = side;
        bottomID = bottom;

        loadAndConvertImages();
        updateCubeTexturesFromData();
    }

    // Load images from disk, convert to editable ARGB arrays
    public void loadAndConvertImages() {
        BufferedImage ti = displaySystem.LoadImage(topID);
        BufferedImage si = displaySystem.LoadImage(sideID);
        BufferedImage bi = displaySystem.LoadImage(bottomID);

        topTex = displaySystem.toARGBArray(ti);
        sideTex = displaySystem.toARGBArray(si);
        bottomTex = displaySystem.toARGBArray(bi);
    }

    // Convert edited ARGB arrays back into images and save
    public void convertAndSaveImages() {
        BufferedImage ti = displaySystem.fromARGBArray(topTex);
        BufferedImage si = displaySystem.fromARGBArray(sideTex);
        BufferedImage bi = displaySystem.fromARGBArray(bottomTex);

        displaySystem.SaveImage(topID, ti);
        displaySystem.SaveImage(sideID, si);
        displaySystem.SaveImage(bottomID, bi);
    }

    // Getters to expose editable data to GUI/inspector
    public int[][] getTopTextureData() {
        return topTex;
    }

    public int[][] getSideTextureData() {
        return sideTex;
    }

    public int[][] getBottomTextureData() {
        return bottomTex;
    }

    // Setters for editable data (if external editor modifies it)
    public void setTopTextureData(int[][] data) {
        topTex = data;
    }

    public void setSideTextureData(int[][] data) {
        sideTex = data;
    }

    public void setBottomTextureData(int[][] data) {
        bottomTex = data;
    }

    public void updateCubeTexturesFromData() {
        cube.setTopFace(convertToTextureRegion(topTex));
        cube.setSideFace(0, convertToTextureRegion(sideTex)); // All 4 sides use same face for now
        cube.setSideFace(1, convertToTextureRegion(sideTex));
        cube.setSideFace(2, convertToTextureRegion(sideTex));
        cube.setSideFace(3, convertToTextureRegion(sideTex));
        cube.setBottomFace(convertToTextureRegion(bottomTex));

        cube.buildCube(); // Ensure it's built once
    }

    private TextureRegion convertToTextureRegion(int[][] argbData) {
        int width = argbData.length;
        int height = argbData[0].length;

        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = argbData[x][y];
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb) & 0xff;

                int rgba = (a << 24) | (r << 16) | (g << 8) | b; // ✅ Correct order
                pixmap.drawPixel(x, y, rgba);
            }
        }

        Texture texture = new Texture(pixmap);
        TextureRegion region = new TextureRegion(texture);
        pixmap.dispose();

        return region;
    }
}
