package com.WorldTool;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.WorldTool.DisplaySystem.DisplaySystem;
import com.WorldTool.SaveSystem.SaveSystem;
import com.WorldTool.UISystem.UISystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class ToolManager implements Screen {

    private UISystem uiSystem;
    private DisplaySystem displaySystem;
    private SaveSystem saveSystem;

    @Override
    public void show() {
        saveSystem = new SaveSystem();
        displaySystem = new DisplaySystem(this);
        uiSystem = new UISystem(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        displaySystem.render(delta);
        uiSystem.render();
    }

    @Override
    public void resize(int width, int height) {
        displaySystem.resize(width, height);
        uiSystem.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        displaySystem.dispose();
        uiSystem.dispose();
    }

    public void ActivateEditor(ToolType toolType) {
        System.out.println("[DEBUG] " + toolType.getLabel() + " initialized.");
        displaySystem.setEditor(toolType);
    }

    // Save System \\

    // Blocks \\

    public void SaveBlocks(Block block) {
        saveSystem.SaveBlocks(block);
    }

    public Block LoadBlock(int id) {
        return saveSystem.LoadBlock(id);
    }

    public Map<Integer, Block> LoadAllBlocks() {
        return saveSystem.LoadAllBlocks();
    }

    // PNG \\

    public void SaveImage(int id, BufferedImage input) {
        saveSystem.SaveImage(id, input);
    }

    public BufferedImage LoadImage(int id) {
        return saveSystem.LoadImage(id);
    }

    // Conversion \\

    public int[][] toARGBArray(BufferedImage input) {
        return saveSystem.toARGBArray(input);
    }

    public BufferedImage fromARGBArray(int[][] argbArray) {
        return saveSystem.fromARGBArray(argbArray);
    }
}
