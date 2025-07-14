package com.WorldTool.UISystem;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ToolBar {

    public ToolBar(UISystem uiSystem) {
        Table root = uiSystem.getRootTable();
        Table toolBar = new Table();
        toolBar.top().right().padTop(10).padRight(10);

        // Add buttons in order, aligned right
        toolBar.add(new TextButton("Entity Tools", uiSystem.getSkin())).pad(5);
        toolBar.add(new TextButton("Prop Tools", uiSystem.getSkin())).pad(5);
        toolBar.add(new TextButton("Animation Tools", uiSystem.getSkin())).pad(5);
        toolBar.add(new TextButton("Pixel Tools", uiSystem.getSkin())).pad(5);
        toolBar.add(new TextButton("Region Tools", uiSystem.getSkin())).pad(5);
        toolBar.add(new TextButton("Structure Tools", uiSystem.getSkin())).pad(5);

        root.add(toolBar).expand().top().right().row();
    }
}
