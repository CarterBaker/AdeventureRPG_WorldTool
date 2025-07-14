package com.WorldTool.UISystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UISystem {
    private final Stage stage;
    private final Skin skin;
    private final Table rootTable;

    public UISystem() {
        stage = new Stage(new ScreenViewport());
        System.out.println(Gdx.files.internal("uiskin.json").file().getAbsolutePath());

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        rootTable = new Table();
        rootTable.setFillParent(true);

        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
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
}
