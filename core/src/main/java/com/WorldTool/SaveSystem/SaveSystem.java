package com.WorldTool.SaveSystem;

import java.nio.file.Paths;

import com.WorldTool.Block;
import com.WorldTool.ImageSystem.ImageSystem;
import java.awt.image.BufferedImage;

import java.util.Map;

public class SaveSystem {

    private String path;

    private SaveBlocks blocks;
    private SaveProps props;
    private SaveItems items;
    private SaveEntities entities;
    private SaveAnimations animations;
    private SaveStructures structures;
    private SaveRegions regions;
    private SaveWorld world;

    private ImageSystem image;

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

    public Block LoadBlock (int ID) {
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
}
