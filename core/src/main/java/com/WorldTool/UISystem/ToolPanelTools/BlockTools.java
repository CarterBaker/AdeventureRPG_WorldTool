package com.WorldTool.UISystem.ToolPanelTools;

import com.WorldTool.UISystem.ToolPanel;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class BlockTools implements ToolProvider {

    private TextField blockField, topField, sideField, bottomField;

    private final ToolPanel toolPanel; // Needs to be passed in externally somehow

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

        topField = new TextField("", skin);
        topField.setMessageText("Top ID");
        panel.add(topField).pad(2).row();

        sideField = new TextField("", skin);
        sideField.setMessageText("Side ID");
        panel.add(sideField).pad(2).row();

        bottomField = new TextField("", skin);
        bottomField.setMessageText("Bottom ID");
        panel.add(bottomField).pad(2).row();

        TextButton applyButton = new TextButton("Apply", skin);
        applyButton.addListener(event -> {
            if (!applyButton.isPressed())
                return false;

            try {
                int block = Integer.parseInt(blockField.getText());
                int top = Integer.parseInt(topField.getText());
                int side = Integer.parseInt(sideField.getText());
                int bottom = Integer.parseInt(bottomField.getText());

                toolPanel.SetTextureIds(top, side, bottom);
                // Optional: set brush color too
                // editor.SetBrushColor(brush);

            } catch (NumberFormatException e) {
                System.out.println("Invalid input in one of the fields.");
            }
            return true;
        });
        panel.add(applyButton).padTop(5).row();
    }
}
