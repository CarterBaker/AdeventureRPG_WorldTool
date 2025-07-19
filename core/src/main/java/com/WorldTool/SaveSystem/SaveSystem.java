package com.WorldTool.SaveSystem;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.Map;

import com.WorldTool.Block;
import com.WorldTool.Region;
import com.WorldTool.WorldTile;
import com.WorldTool.ImageSystem.ImageSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SaveSystem {

    private final String path;

    private final SaveBlocks blocks;
    private final SaveProps props;
    private final SaveItems items;
    private final SaveEntities entities;
    private final SaveAnimations animations;
    private final SaveStructures structures;
    private final SaveRegions regions;
    private final SaveWorld world;

    private final ImageSystem image;

    public SaveSystem() {

        String userHome = System.getProperty("user.home");
        this.path = Paths.get(userHome, "Documents", "development", "data").toString();

        this.blocks = new SaveBlocks(path);
        this.props = new SaveProps();
        this.items = new SaveItems();
        this.entities = new SaveEntities();
        this.animations = new SaveAnimations();
        this.structures = new SaveStructures();
        this.regions = new SaveRegions(path);
        this.world = new SaveWorld(path);

        this.image = new ImageSystem(path);
    }

    // Blocks \\

    public void SaveBlocks(Block block) {
        blocks.Save(block);
    }

    public Block LoadBlock(int ID) {
        return blocks.Load(ID);
    }

    public Map<Integer, Block> LoadAllBlocks() {
        return blocks.LoadAll();
    }

    // Props \\

    public void SaveProps() {
        props.Save();
    }

    // Items \\

    public void SaveItems() {
        items.Save();
    }

    // Entities \\

    public void SaveEntities() {
        entities.Save();
    }

    // Animations \\

    public void SaveAnimations() {
        animations.Save();
    }

    // Structures \\

    public void SaveStructures() {
        structures.Save();
    }

    // Regions \\

    public void SaveRegion(Region region) {
        regions.Save(region);
    }

    public Region LoadRegion(int ID) {
        return regions.Load(ID);
    }

    public Map<Integer, Region> LoadAllRegions() {
        return regions.LoadAll();
    }

    // World \\

    public void SaveWorld(WorldTile[][] worldTile) {
        world.Save(worldTile);
    }

    public WorldTile[][] LoadWorld() {
        return world.Load();
    }

    // PNG \\

    public void SaveImage(int id, BufferedImage input) {
        image.Save(id, input);
    }

    public BufferedImage LoadImage(int id) {
        return image.Load(id);
    }

    // Conversion \\

    public int[][] toARGBArray(BufferedImage input) {
        return image.toARGBArray(input);
    }

    public BufferedImage fromARGBArray(int[][] argbArray) {
        return image.fromARGBArray(argbArray);
    }

    public TextureRegion convertToTextureRegion(int[][] input) {
        return image.convertToTextureRegion(input);
    }
}
