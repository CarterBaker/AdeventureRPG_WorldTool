package com.WorldTool.UISystem.ToolPanelTools;

import com.WorldTool.Region;
import com.WorldTool.WorldTile;
import com.WorldTool.DisplaySystem.EditorTools.WorldToolType;
import com.WorldTool.UISystem.ToolPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Map;

public class WorldTools implements ToolProvider {

    private ToolPanel toolPanel;

    // New input fields for scale
    private TextField scaleXField;
    private TextField scaleYField;

    public WorldTools() {
    }

    public WorldTools(ToolPanel toolPanel) {
        this.toolPanel = toolPanel;
    }

    @Override
    public void populateTools(Table panel, Skin skin) {
        panel.top();
        panel.add(new Label("World Tools", skin)).pad(5).row();

        if (toolPanel == null)
            return;

        // --- World Scale Input ---
        scaleXField = new TextField("", skin);
        scaleYField = new TextField("", skin);

        Table scaleTable = new Table();
        scaleTable.add(new Label("Scale X:", skin)).pad(2);
        scaleTable.add(scaleXField).width(50).pad(2);
        scaleTable.add(new Label("Scale Y:", skin)).pad(2);
        scaleTable.add(scaleYField).width(50).pad(2);

        TextButton setScaleButton = new TextButton("Set Scale", skin);
        setScaleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int xScale = Integer.parseInt(scaleXField.getText());
                    int yScale = Integer.parseInt(scaleYField.getText());
                    toolPanel.SetWorldScale(xScale, yScale);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid scale input");
                }
            }
        });

        panel.add(scaleTable).left().pad(5).row();
        panel.add(setScaleButton).left().pad(5).row();

        // --- World ToolType Dropdown ---
        final SelectBox<WorldToolType> toolTypeSelect = new SelectBox<>(skin);
        toolTypeSelect.setItems(WorldToolType.values());

        toolTypeSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WorldToolType selected = toolTypeSelect.getSelected();
                if (selected != null) {
                    toolPanel.SetWorldToolType(selected);
                }
            }
        });

        Table toolTypeTable = new Table();
        toolTypeTable.add(new Label("Tool Type:", skin)).pad(2);
        toolTypeTable.add(toolTypeSelect).width(150).pad(2);

        panel.add(toolTypeTable).left().pad(5).row();

        // --- Save and Load World Buttons ---

        TextButton saveWorldButton = new TextButton("Save World", skin);
        saveWorldButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Assuming you have access to the current world tiles from toolPanel or
                // elsewhere
                WorldTile[][] currentWorld = toolPanel.GetCurrentWorldTiles(); // You'll need this method
                if (currentWorld != null) {
                    toolPanel.SaveWorld(currentWorld);
                }
            }
        });

        TextButton loadWorldButton = new TextButton("Load World", skin);
        loadWorldButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WorldTile[][] loadedWorld = toolPanel.LoadWorld();
                if (loadedWorld != null) {
                    // Handle the loaded world, for example, tell UI system or refresh display
                    toolPanel.SetWorldTiles(loadedWorld); // Example method - implement as needed
                }
            }
        });

        Table saveLoadTable = new Table();
        saveLoadTable.add(saveWorldButton).pad(5).fillX().expandX();
        saveLoadTable.row();
        saveLoadTable.add(loadWorldButton).pad(5).fillX().expandX();

        panel.add(saveLoadTable).left().pad(5).row();

        // --- Region Color Buttons ---
        Map<Integer, Region> regions = toolPanel.LoadAllRegions();
        for (Map.Entry<Integer, Region> entry : regions.entrySet()) {
            Region region = entry.getValue();

            Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
            pixmap.setColor(argbToColor(region.color));
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();

            TextureRegionDrawable drawable = new TextureRegionDrawable(texture);

            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            style.up = drawable;
            style.down = drawable;
            style.checked = drawable;
            style.font = skin.getFont("default-font");

            TextButton colorButton = new TextButton("", style);
            colorButton.setSize(20, 20);
            colorButton.pad(0);

            Label nameLabel = new Label(region.name, skin);
            nameLabel.setEllipsis(true);
            nameLabel.setWrap(false);
            nameLabel.setWidth(100);

            Table row = new Table();
            row.add(colorButton).size(20, 20).pad(2);
            row.add(nameLabel).width(100).left().pad(2);

            panel.add(row).left().row();

            colorButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    toolPanel.SetBrushColor(region.color);
                    toolPanel.SetCurrentRegion(region);
                }
            });
        }

        panel.pack();
        panel.invalidateHierarchy();

    }

    private static Color argbToColor(int argb) {
        int a = (argb >> 24) & 0xff;
        int r = (argb >> 16) & 0xff;
        int g = (argb >> 8) & 0xff;
        int b = (argb) & 0xff;
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}
