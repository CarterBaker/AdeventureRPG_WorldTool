package com.WorldTool.UISystem;

import java.util.Map;

import com.WorldTool.Block;
import com.WorldTool.Region;
import com.WorldTool.ToolType;
import com.WorldTool.WorldTile;
import com.WorldTool.DisplaySystem.EditorTools.WorldToolType;
import com.WorldTool.UISystem.ToolPanelTools.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.awt.image.BufferedImage;

public class ToolPanel extends Table {

    private UISystem uiSystem;

    private final float topOffset;
    private final Texture backgroundTexture;
    private Skin skin;

    public ToolPanel(UISystem input, float height) {

        this.uiSystem = input;
        this.topOffset = height;

        skin = uiSystem.getSkin();

        // Create a 1x1 pixmap with light gray color for background
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
        pixmap.dispose();

        this.setBackground(new TextureRegionDrawable(backgroundTexture));
        this.setWidth(200); // fixed width

        this.top();

        // Add to stage
        uiSystem.getStage().addActor(this);

        // Initial size and position
        updateSizeAndPosition();

        // Optional: resize listener if you want dynamic resizing
        uiSystem.getStage().addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean scrolled(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, float amountX,
                    float amountY) {
                updateSizeAndPosition();
                return false;
            }
        });
    }

    private void updateSizeAndPosition() {
        float screenHeight = Gdx.graphics.getHeight();
        float usableHeight = screenHeight - topOffset;
        this.setHeight(usableHeight);
        this.setPosition(0, 0); // aligned to bottom-left
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateSizeAndPosition(); // Continuously ensure correct size
    }

    @Override
    protected void finalize() throws Throwable {
        if (backgroundTexture != null)
            backgroundTexture.dispose();
    }

    public void SwitchTools(ToolType type) {
        this.clearChildren(); // clear previous tools

        ToolProvider provider = getToolProvider(type);
        if (provider != null) {
            provider.populateTools(this, skin);
        } else {
            this.add(new Label("No tools found", skin));
        }
    }

    private ToolProvider getToolProvider(ToolType type) {
        return switch (type) {
            case BLOCK -> new BlockTools(this);
            case PROP -> new PropTools();
            case ITEM -> new ItemTools();
            case ENTITY -> new EntityTools();
            case ANIMATION -> new AnimationTools();
            case STRUCTURE -> new StructureTools();
            case REGION -> new RegionTools(this);
            case WORLD -> new WorldTools(this);
            default -> null;
        };
    }

    // References \\

    // Blocks \\

    public void SetBrushColor(int input) {
        uiSystem.SetBrushColor(input);
    }

    public void SetTextureIds(int top, int side, int bottom) {
        uiSystem.SetTextureIds(top, side, bottom);
    }

    public void convertAndSaveImages() {
        uiSystem.convertAndSaveImages();
    }

    public void SaveBlocks(Block block) {
        uiSystem.SaveBlocks(block);
    }

    public Block LoadBlock(int ID) {
        return uiSystem.LoadBlock(ID);
    }

    public Map<Integer, Block> LoadAllBlocks() {
        return uiSystem.LoadAllBlocks();
    }

    // Regions \\

    public void SaveRegion(Region region) {
        uiSystem.SaveRegion(region);
    }

    public Region LoadRegion(int ID) {
        return uiSystem.LoadRegion(ID);
    }

    public Map<Integer, Region> LoadAllRegions() {
        return uiSystem.LoadAllRegions();
    }

    // World \\

    public void SetWorldToolType(WorldToolType input) {
        uiSystem.SetWorldToolType(input);
    }

    public void SetWorldScale(int x, int y) {
        uiSystem.SetWorldScale(x, y);
    }

    public void SetCurrentRegion(Region region) {
        uiSystem.SetCurrentRegion(region);
    }

    public WorldTile[][] GetCurrentWorldTiles() {
        return uiSystem.GetCurrentWorldTiles();
    }

    public void SetWorldTiles(WorldTile[][] input) {
        uiSystem.SetWorldTiles(input);
    }

    public void SaveWorld(WorldTile[][] worldTile) {
        uiSystem.SaveWorld(worldTile);
    }

    public WorldTile[][] LoadWorld() {
        return uiSystem.LoadWorld();
    }

    // PNG \\

    public void SaveImage(int id, BufferedImage input) {
        uiSystem.SaveImage(id, input);
    }

    public BufferedImage LoadImage(int id) {
        return uiSystem.LoadImage(id);
    }
}
