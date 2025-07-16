package com.WorldTool.ImageSystem;

import com.badlogic.gdx.math.Vector2;

public class BlockEditorTools {

    private final ImageEditorTools editingTools;

    private final Vector2 topPosition = new Vector2(50, 400); // Adjust Y as needed
    private final Vector2 sidePosition = new Vector2(150, 400); // X offset by +100
    private final Vector2 bottomPosition = new Vector2(250, 400); // X offset by +100 again

    public void render(float delta, boolean isDragging) {
        editingTools.render(delta, isDragging);
    }

    public BlockEditorTools() {
        this.editingTools = new ImageEditorTools();
    }

    public int[][] drawTopImage(int id, int[][] input, boolean handleInput, int color) {
        input = editingTools.drawEditableImage(id, input, topPosition, handleInput, color);
        return input;
    }

    public int[][] drawSideImage(int id, int[][] input, boolean handleInput, int color) {
        input = editingTools.drawEditableImage(id, input, sidePosition, handleInput, color);
        return input;
    }

    public int[][] drawBottomImage(int id, int[][] input, boolean handleInput, int color) {
        input = editingTools.drawEditableImage(id, input, bottomPosition, handleInput, color);
        return input;
    }
}
