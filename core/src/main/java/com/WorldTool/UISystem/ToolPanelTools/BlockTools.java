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

        // --- Save Button ---
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
                    // Optionally provide visual feedback or logging
                    System.out.println("Invalid input. Block not saved.");
                }
            }
        });
    }
}
