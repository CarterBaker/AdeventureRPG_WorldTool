package com.WorldTool.DisplaySystem;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.WorldTool.Block;
import com.WorldTool.Region;
import com.WorldTool.ToolManager;
import com.WorldTool.ToolType;
import com.WorldTool.WorldTile;
import com.WorldTool.DisplaySystem.EditorTools.WorldToolType;
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
        worldEditor = new WorldEditor(input.LoadAllRegions());

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
        currentEditor = switch (toolType) {
            case BLOCK -> blockEditor;
            case PROP -> propEditor;
            case ITEM -> itemEditor;
            case ENTITY -> entityEditor;
            case ANIMATION -> animationEditor;
            case STRUCTURE -> structureEditor;
            case REGION -> regionEditor;
            case WORLD -> worldEditor;
            default -> null;
        };
    }

    public void resize(int width, int height) {
        if (currentEditor != null) {
            currentEditor.resize(width, height);
        }
    }

    public void dispose() {
        batch.dispose();
    }

    // ReferencesS \\

    // Blocks \\

    public void SetBrushColor(int input) {
        blockEditor.SetBrushColor(input);
    }

    public void SetTextureIds(int top, int side, int bottom) {
        blockEditor.SetTextureIds(top, side, bottom);
    }

    public void convertAndSaveImages() {
        blockEditor.convertAndSaveImages();
    }

    public void SaveBlocks(Block block) {
        toolManager.SaveBlocks(block);
    }

    public Block LoadBlock(int id) {
        return toolManager.LoadBlock(id);
    }

    // Regions \\

    public void SaveRegion(Region region) {
        toolManager.SaveRegion(region);
    }

    public Region LoadRegion(int ID) {
        return toolManager.LoadRegion(ID);
    }

    public Map<Integer, Region> LoadAllRegions() {
        return toolManager.LoadAllRegions();
    }

    // World \\

    public void SetWorldToolType (WorldToolType input) {
        worldEditor.SetToolType(input);
    }

    public void SetWorldScale(int x, int y) {
        worldEditor.SetWorldScale(x, y);
    }

    public void SetCurrentRegion(Region region) {
        worldEditor.SetCurrentRegion(region);
    }

    public WorldTile[][] GetCurrentWorldTiles() {
        return worldEditor.GetCurrentWorldTiles();
    }

    public void SetWorldTiles(WorldTile[][] input) {
        worldEditor.SetWorldTiles(input);
    }

    public void SaveWorld(WorldTile[][] worldTile) {
        toolManager.SaveWorld(worldTile);
    }

    public WorldTile[][] LoadWorld() {
        return toolManager.LoadWorld();
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
