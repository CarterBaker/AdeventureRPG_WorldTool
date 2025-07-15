package com.WorldTool.UISystem.ToolPanelTools;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public class AnimationTools implements ToolProvider {
    @Override
    public void populateTools(Table panel, Skin skin) {
        panel.top();
        
        panel.add(new Label("Animation Tools", skin)).pad(5).row();
    }
}
