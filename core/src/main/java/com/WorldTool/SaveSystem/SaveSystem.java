package com.WorldTool.SaveSystem;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.Map;

import com.WorldTool.Block;
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
        this.regions = new SaveRegions();
        this.world = new SaveWorld();

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

    public void SaveItems() {
        items.Save();
    }

    public void SaveEntities() {
        entities.Save();
    }

    public void SaveAnimations() {
        animations.Save();
    }

    public void SaveStructures() {
        structures.Save();
    }

    public void SaveRegions() {
        regions.Save();
    }

    public void SaveWorld() {
        world.Save();
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
