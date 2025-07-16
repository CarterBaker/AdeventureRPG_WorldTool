package com.WorldTool.ImageSystem;

public class BlockEditorTools {

    private final ImageEditorTools editingTools;


    public void render(float delta, boolean isDragging) {
        editingTools.render(delta, isDragging);
    }

    public BlockEditorTools() {
        this.editingTools = new ImageEditorTools();
    }

    public int[][] drawTopImage(int id, int[][] input, boolean handleInput, int color) {
        input = editingTools.drawEditableImage(id, input, handleInput, color);
        return input;
    }

    public int[][] drawSideImage(int id, int[][] input, boolean handleInput, int color) {
        input = editingTools.drawEditableImage(id, input, handleInput, color);
        return input;
    }

    public int[][] drawBottomImage(int id, int[][] input, boolean handleInput, int color) {
        input = editingTools.drawEditableImage(id, input, handleInput, color);
        return input;
    }
}
