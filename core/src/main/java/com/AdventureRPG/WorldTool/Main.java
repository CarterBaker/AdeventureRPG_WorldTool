package com.AdventureRPG.WorldTool;

import com.WorldTool.ToolManager;
import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;

public class Main extends Game {
    @Override
    public void create() {
        VisUI.load();
        this.setScreen(new ToolManager());
    }
}
