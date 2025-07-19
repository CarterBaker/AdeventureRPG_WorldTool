package com.WorldTool.UISystem.ToolPanelTools;

import com.WorldTool.Region;
import com.WorldTool.UISystem.ToolPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class RegionTools implements ToolProvider {

    private TextField regionIdField, nameField, colorField;
    private int currentColorARGB = 0xFFFFFFFF; // default white
    private final ToolPanel toolPanel;

    public RegionTools(ToolPanel input) {
        this.toolPanel = input;
    }

    @Override
    public void populateTools(Table panel, Skin skin) {
        panel.top();

        panel.add(new Label("Region Tools", skin)).pad(5).row();

        regionIdField = new TextField("", skin);
        regionIdField.setMessageText("Region ID");
        panel.add(regionIdField).pad(2).row();

        TextButton loadButton = new TextButton("Load Region", skin);
        panel.add(loadButton).pad(2).row();

        nameField = new TextField("", skin);
        nameField.setMessageText("Region Name");
        panel.add(nameField).pad(2).row();

        colorField = new TextField("", skin);
        colorField.setMessageText("ARGB (hex, e.g. FF00FF00)");
        panel.add(colorField).pad(2).row();

        VisTextButton colorPickerButton = new VisTextButton("Pick Color");
        panel.add(colorPickerButton).pad(2).row();

        ColorPicker colorPicker = new ColorPicker("Select Region Color");
        colorPicker.setListener(new ColorPickerAdapter() {
            @Override
            public void finished(Color color) {
                int a = (int) (color.a * 255);
                int r = (int) (color.r * 255);
                int g = (int) (color.g * 255);
                int b = (int) (color.b * 255);

                currentColorARGB = (a << 24) | (r << 16) | (g << 8) | b;
                colorField.setText(String.format("%08X", currentColorARGB));
                toolPanel.SetBrushColor(currentColorARGB);
            }
        });

        colorPickerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toolPanel.getStage().addActor(colorPicker.fadeIn());
            }
        });

        // Load Button Logic
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int id = Integer.parseInt(regionIdField.getText());
                    Region region = toolPanel.LoadRegion(id);
                    if (region != null) {
                        nameField.setText(region.name);
                        colorField.setText(String.format("%08X", region.color));
                        currentColorARGB = region.color;
                        toolPanel.SetBrushColor(currentColorARGB);
                    } else {
                        nameField.setText("?");
                        colorField.setText("?");
                    }
                } catch (NumberFormatException e) {
                    nameField.setText("!");
                    colorField.setText("!");
                }
            }
        });

        // Save Button Logic
        TextButton saveButton = new TextButton("Save Region", skin);
        panel.add(saveButton).pad(4).row();

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Region region = new Region();
                    region.ID = Integer.parseInt(regionIdField.getText());
                    region.name = nameField.getText();
                    region.color = (int) Long.parseLong(colorField.getText(), 16);
                    toolPanel.SaveRegion(region);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Region not saved.");
                }
            }
        });
    }
}
