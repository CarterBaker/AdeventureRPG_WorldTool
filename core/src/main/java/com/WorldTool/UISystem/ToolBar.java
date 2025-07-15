package com.WorldTool.UISystem;

import com.WorldTool.ToolType;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ToolBar {

    public ToolBar(UISystem uiSystem, float height) {
        Skin skin = uiSystem.getSkin();
        Table root = uiSystem.getRootTable();

        // Top bar container
        Table topBar = new Table();
        topBar.setBackground(skin.newDrawable("white", 1f, 1f, 1f, 1f));
        topBar.right();
        topBar.setHeight(height);

        // Right-side tools dropdown
        SelectBox<ToolType> toolsDropdown = new SelectBox<>(skin);
        toolsDropdown.setItems(ToolType.values());
        toolsDropdown.setSelected(ToolType.fromLabel("Block Editor"));

        toolsDropdown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ToolType selected = toolsDropdown.getSelected();
                uiSystem.ActivateEditor(selected);
            }
        });

        topBar.add(toolsDropdown).pad(5).right();

        // Add the top bar to the stage
        root.add(topBar).expandX().top().fillX().height(height).row();
        root.add().expand().fill();

        // Set the default editor type
        toolsDropdown.setSelected(ToolType.BLOCK);
        uiSystem.ActivateEditor(ToolType.BLOCK);
    }
}
