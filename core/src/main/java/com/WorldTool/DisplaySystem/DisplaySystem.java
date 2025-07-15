package com.WorldTool.DisplaySystem;

import com.WorldTool.ToolType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DisplaySystem {

    private SpriteBatch batch;

    private BlockEditor blockEditor;
    private PropEditor propEditor;
    private ItemEditor itemEditor;
    private EntityEditor entityEditor;
    private AnimationEditor animationEditor;
    private StructureEditor structureEditor;
    private RegionEditor regionEditor;
    private WorldEditor worldEditor;

    private Editor currentEditor;

    public DisplaySystem() {
        batch = new SpriteBatch();

        blockEditor = new BlockEditor();
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
            case BLOCK: currentEditor = blockEditor; break;
            case PROP: currentEditor = propEditor; break;
            case ITEM: currentEditor = itemEditor; break;
            case ENTITY: currentEditor = entityEditor; break;
            case ANIMATION: currentEditor = animationEditor; break;
            case STRUCTURE: currentEditor = structureEditor; break;
            case REGION: currentEditor = regionEditor; break;
            case WORLD: currentEditor = worldEditor; break;
            default: currentEditor = null;
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
}
