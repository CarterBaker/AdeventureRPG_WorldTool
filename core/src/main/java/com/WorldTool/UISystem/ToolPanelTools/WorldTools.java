package com.WorldTool.UISystem.ToolPanelTools;

import com.WorldTool.Region;
import com.WorldTool.UISystem.ToolPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Map;

public class WorldTools implements ToolProvider {

    private ToolPanel toolPanel;

    // New input fields for scale
    private TextField scaleXField;
    private TextField scaleYField;

    public WorldTools() {}

    public WorldTools(ToolPanel toolPanel) {
        this.toolPanel = toolPanel;
    }

    @Override
    public void populateTools(Table panel, Skin skin) {
        panel.top();
        panel.add(new Label("World Tools", skin)).pad(5).row();

        if (toolPanel == null) return;

        // Add fields for World Scale
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

            Label nameLabel = new Label(region.name, skin);

            Table row = new Table();
            row.add(colorButton).size(20, 20).pad(2);
            row.add(nameLabel).left().pad(2);

            panel.add(row).left().row();

            colorButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    toolPanel.SetBrushColor(region.color);
                    toolPanel.SetCurrentRegion(region); // Also notify toolPanel of region
                }
            });
        }
    }

    private static Color argbToColor(int argb) {
        int a = (argb >> 24) & 0xff;
        int r = (argb >> 16) & 0xff;
        int g = (argb >> 8) & 0xff;
        int b = (argb) & 0xff;
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}
