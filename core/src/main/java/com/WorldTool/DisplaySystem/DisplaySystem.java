package com.WorldTool.DisplaySystem;

import java.awt.image.BufferedImage;

import com.WorldTool.Block;
import com.WorldTool.ToolManager;
import com.WorldTool.ToolType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DisplaySystem {

    private final ToolManager toolManager;

    private final SpriteBatch batch;

    private final BlockEditor blockEditor;
    private final PropEditor propEditor;
    private final ItemEditor itemEditor;
    private final EntityEditor entityEditor;
    private final AnimationEditor animationEditor;
    private final StructureEditor structureEditor;
    private final RegionEditor regionEditor;
    private final WorldEditor worldEditor;

    private Editor currentEditor;

    public DisplaySystem(ToolManager input) {
        this.toolManager = input;

        batch = new SpriteBatch();

        blockEditor = new BlockEditor(this);
        propEditor = new PropEditor();
        itemEditor = new ItemEditor();
        entityEditor = new EntityEditor();
        animationEditor = new AnimationEditor();
        structureEditor = new StructureEditor();
        regionEditor = new RegionEditor();
        worldEditor = new WorldEditor();

        currentEditor = null;
    }

    public void render(float delta) {
        batch.begin();
        if (currentEditor != null) {
            currentEditor.render(batch, delta);
        }
        batch.end();
    }

    public void setEditor(ToolType toolType) {
        switch (toolType) {
            case BLOCK:
                currentEditor = blockEditor;
                break;
            case PROP:
                currentEditor = propEditor;
                break;
            case ITEM:
                currentEditor = itemEditor;
                break;
            case ENTITY:
                currentEditor = entityEditor;
                break;
            case ANIMATION:
                currentEditor = animationEditor;
                break;
            case STRUCTURE:
                currentEditor = structureEditor;
                break;
            case REGION:
                currentEditor = regionEditor;
                break;
            case WORLD:
                currentEditor = worldEditor;
                break;
            default:
                currentEditor = null;
        }
    }

    public void resize(int width, int height) {
        if (currentEditor != null) {
            currentEditor.resize(width, height);
        }
    }

    public void dispose() {
        batch.dispose();
    }

    // Save System \\

    // Blocks \\

    public void SaveBlocks(Block block) {
        toolManager.SaveBlocks(block);
    }

    public Block LoadBlock(int id) {
        return toolManager.LoadBlock(id);
    }

    // PNG \\

    public void SaveImage(int id, BufferedImage input) {
        toolManager.SaveImage(id, input);
    }

    public BufferedImage LoadImage(int id) {
        return toolManager.LoadImage(id);
    }

    // Conversion \\

    public int[][] toARGBArray(BufferedImage input) {
        return toolManager.toARGBArray(input);
    }

    public BufferedImage fromARGBArray(int[][] argbArray) {
        return toolManager.fromARGBArray(argbArray);
    }

    public TextureRegion convertToTextureRegion(int[][] input) {
        return toolManager.convertToTextureRegion(input);
    }
}
