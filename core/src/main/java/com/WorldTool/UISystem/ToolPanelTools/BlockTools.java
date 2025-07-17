package com.WorldTool.UISystem.ToolPanelTools;

import com.WorldTool.Block;
import com.WorldTool.UISystem.ToolPanel;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class BlockTools implements ToolProvider {

    private TextField blockField, topField, sideField, bottomField;
    private final ToolPanel toolPanel;

    public BlockTools(ToolPanel input) {
        this.toolPanel = input;
    }

    @Override
    public void populateTools(Table panel, Skin skin) {
        panel.top();
        panel.add(new Label("Block Tools", skin)).pad(5).row();

        blockField = new TextField("", skin);
        blockField.setMessageText("Block ID");
        panel.add(blockField).pad(2).row();

        TextButton loadButton = new TextButton("Load Block", skin);
        panel.add(loadButton).pad(2).row();

        topField = new TextField("", skin);
        topField.setMessageText("Top ID");
        panel.add(topField).pad(2).row();

        sideField = new TextField("", skin);
        sideField.setMessageText("Side ID");
        panel.add(sideField).pad(2).row();

        bottomField = new TextField("", skin);
        bottomField.setMessageText("Bottom ID");
        panel.add(bottomField).pad(2).row();

        // Load Block Button Logic
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int id = Integer.parseInt(blockField.getText());
                    Block block = toolPanel.LoadBlock(id);
                    if (block != null) {
                        topField.setText(String.valueOf(block.top));
                        sideField.setText(String.valueOf(block.sides));
                        bottomField.setText(String.valueOf(block.bottom));
                        toolPanel.SetTextureIds(block.top, block.sides, block.bottom);
                    } else {
                        topField.setText("?");
                        sideField.setText("?");
                        bottomField.setText("?");
                    }
                } catch (NumberFormatException e) {
                    topField.setText("!");
                    sideField.setText("!");
                    bottomField.setText("!");
                }
            }
        });

        // --- Save Block Button ---
        TextButton saveButton = new TextButton("Save Block", skin);
        panel.add(saveButton).pad(4).row();

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Block block = new Block();
                    block.ID = Integer.parseInt(blockField.getText());
                    block.top = Integer.parseInt(topField.getText());
                    block.sides = Integer.parseInt(sideField.getText());
                    block.bottom = Integer.parseInt(bottomField.getText());
                    toolPanel.SaveBlocks(block);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Block not saved.");
                }
            }
        });

        // --- Save All Images Button ---
        TextButton saveAllImagesButton = new TextButton("Save All Images", skin);
        panel.add(saveAllImagesButton).pad(4).row();

        saveAllImagesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toolPanel.convertAndSaveImages();
            }
        });

        // --- Color Picker ---
        VisTextButton colorPickerButton = new VisTextButton("Open Color Picker");
        panel.add(colorPickerButton).pad(4).row();

        ColorPicker colorPicker = new ColorPicker("Choose Brush Color");

        colorPicker.setListener(new ColorPickerAdapter() {
            @Override
            public void finished(com.badlogic.gdx.graphics.Color color) {
                // Convert RGBA float [0–1] to 0xRRGGBB integer
                int a = (int) (color.a * 255);
                int r = (int) (color.r * 255);
                int g = (int) (color.g * 255);
                int b = (int) (color.b * 255);

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                toolPanel.SetBrushColor(argb);
            }
        });

        colorPickerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toolPanel.getStage().addActor(colorPicker.fadeIn());
            }
        });

    }

}
