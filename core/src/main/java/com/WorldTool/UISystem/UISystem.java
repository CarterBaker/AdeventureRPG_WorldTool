package com.WorldTool.UISystem;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.WorldTool.Block;
import com.WorldTool.Region;
import com.WorldTool.ToolManager;
import com.WorldTool.ToolType;
import com.WorldTool.WorldTile;
import com.WorldTool.DisplaySystem.EditorTools.WorldToolType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UISystem {
    private final ToolManager toolManager;
    private final Stage stage;
    private final Skin skin;
    private final Table rootTable;

    private ToolPanel toolPanel;

    private float ToolBarHeight = 20f;

    public UISystem(ToolManager base) {
        toolManager = base;
        stage = new Stage(new ScreenViewport());
        System.out.println(Gdx.files.internal("uiskin.json").file().getAbsolutePath());

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        rootTable = new Table();
        rootTable.setFillParent(true);

        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);

        setupUI();
    }

    private void setupUI() {
        toolPanel = new ToolPanel(this, ToolBarHeight);
        new ToolBar(this, ToolBarHeight);
    }

    public Stage getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }

    public Table getRootTable() {
        return rootTable;
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public void ActivateEditor(ToolType type) {
        toolManager.ActivateEditor(type);
        toolPanel.SwitchTools(type);
    }

    // References \\

    // Blocks \\

    public void SetBrushColor(int input) {
        toolManager.SetBrushColor(input);
    }

    public void SetTextureIds(int top, int side, int bottom) {
        toolManager.SetTextureIds(top, side, bottom);
    }

    public void convertAndSaveImages() {
        toolManager.convertAndSaveImages();
    }

    public void SaveBlocks(Block block) {
        toolManager.SaveBlocks(block);
    }

    public Block LoadBlock(int ID) {
        return toolManager.LoadBlock(ID);
    }

    public Map<Integer, Block> LoadAllBlocks() {
        return toolManager.LoadAllBlocks();
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

    public void SetWorldToolType(WorldToolType input) {
        toolManager.SetWorldToolType(input);
    }

    public void SetWorldScale(int x, int y) {
        toolManager.SetWorldScale(x, y);
    }

    public void SetCurrentRegion(Region region) {
        toolManager.SetCurrentRegion(region);
    }

    public WorldTile[][] GetCurrentWorldTiles() {
        return toolManager.GetCurrentWorldTiles();
    }

    public void SetWorldTiles(WorldTile[][] input) {
        toolManager.SetWorldTiles(input);
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
}
