package com.WorldTool;

import com.WorldTool.UISystem.UISystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class ToolManager implements Screen {

    private UISystem uiSystem;

    @Override
    public void show() {
        uiSystem = new UISystem();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiSystem.render(); // Draw UI system (includes toolbar)
    }

    @Override
    public void resize(int width, int height) {
        uiSystem.resize(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        uiSystem.dispose();
    }
}
