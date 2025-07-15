package com.WorldTool.DisplaySystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Editor {
    void render(SpriteBatch batch, float delta);
    void resize(int width, int height);
}
