package com.WorldTool.ImageSystem;

import com.badlogic.gdx.math.Vector2;

public class BlockEditorTools {

    private final ImageEditorTools editingTools;

    private final Vector2 topPosition = new Vector2(50, 400); // Adjust Y as needed
    private final Vector2 sidePosition = new Vector2(150, 400); // X offset by +100
    private final Vector2 bottomPosition = new Vector2(250, 400); // X offset by +100 again

    public BlockEditorTools() {
        this.editingTools = new ImageEditorTools();
    }

    public int[][] drawTopImage(int[][] input, boolean handleInput) {
        input = editingTools.drawEditableImage(input, topPosition, handleInput);
        return input;
    }

    public int[][] drawSideImage(int[][] input, boolean handleInput) {
        input = editingTools.drawEditableImage(input, sidePosition, handleInput);
        return input;
    }

    public int[][] drawBottomImage(int[][] input, boolean handleInput) {
        input = editingTools.drawEditableImage(input, bottomPosition, handleInput);
        return input;
    }
}
