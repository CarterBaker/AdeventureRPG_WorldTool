package com.WorldTool.UISystem;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.WorldTool.Block;
import com.WorldTool.ToolManager;
import com.WorldTool.ToolType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UISystem {
    private final ToolManager tooleManager;
    private final Stage stage;
    private final Skin skin;
    private final Table rootTable;

    private ToolPanel toolPanel;

    private float ToolBarHeight = 20f;

    public UISystem(ToolManager base) {
        tooleManager = base;
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
        tooleManager.ActivateEditor(type);
        toolPanel.SwitchTools(type);
    }

    // References \\

    // Blocks \\

    public void SetTextureIds(int top, int side, int bottom) {
        tooleManager.SetTextureIds(top, side, bottom);
    }

    public void SaveBlocks(Block block) {
        tooleManager.SaveBlocks(block);
    }

    public Block LoadBlock(int ID) {
        return tooleManager.LoadBlock(ID);
    }

    public Map<Integer, Block> LoadAllBlocks() {
        return tooleManager.LoadAllBlocks();
    }

    // PNG \\

    public void SaveImage(int id, BufferedImage input) {
        tooleManager.SaveImage(id, input);
    }

    public BufferedImage LoadImage(int id) {
        return tooleManager.LoadImage(id);
    }
}
