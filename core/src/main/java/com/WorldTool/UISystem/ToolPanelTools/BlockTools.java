package com.WorldTool.UISystem.ToolPanelTools;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public class BlockTools implements ToolProvider {
    @Override
    public void populateTools(Table panel, Skin skin) {
        panel.top();

        panel.add(new Label("Block Tools", skin)).pad(5).row();
    }
}
