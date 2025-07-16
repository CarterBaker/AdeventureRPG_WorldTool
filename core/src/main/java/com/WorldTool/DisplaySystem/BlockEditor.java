package com.WorldTool.DisplaySystem;

import java.awt.image.BufferedImage;

import com.WorldTool.DisplaySystem.EditorTools.SimpleCubeRenderer;
import com.WorldTool.ImageSystem.BlockEditorTools;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class BlockEditor implements Editor {

    private final DisplaySystem displaySystem;

    private final SimpleCubeRenderer cube;
    private final BlockEditorTools imageTools;
    private final Vector2 cubePosition = new Vector2(250, 75);

    private int topID = 0;
    private int sideID = 0;
    private int bottomID = 0;

    private int[][] topTex;
    private int[][] sideTex;
    private int[][] bottomTex;

    private int brushColor;

    public BlockEditor(DisplaySystem input) {
        this.displaySystem = input;
        this.cube = new SimpleCubeRenderer(cubePosition);
        this.imageTools = new BlockEditorTools();

        SetTextureIds(0, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        cube.render(delta);
        imageTools.render(delta, cube.isDragging());

        UpdateCurrentTextures();
    }

    @Override
    public void resize(int width, int height) {
        // Optional: handle resize
    }

    private void SetBrushColor(int input) {
        brushColor = input;
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

    public void UpdateCurrentTextures() {
        topTex = imageTools.drawTopImage(topID, topTex, true, brushColor);
        sideTex = imageTools.drawSideImage(sideID, sideTex, true, brushColor);
        bottomTex = imageTools.drawBottomImage(bottomID, bottomTex, true, brushColor);

        updateCubeTexturesFromData();
    }

    public void updateCubeTexturesFromData() {
        cube.setTopFace(displaySystem.convertToTextureRegion(topTex));
        cube.setSideFace(0, displaySystem.convertToTextureRegion(sideTex));
        cube.setSideFace(1, displaySystem.convertToTextureRegion(sideTex));
        cube.setSideFace(2, displaySystem.convertToTextureRegion(sideTex));
        cube.setSideFace(3, displaySystem.convertToTextureRegion(sideTex));
        cube.setBottomFace(displaySystem.convertToTextureRegion(bottomTex));

        cube.buildCube();
    }
}
