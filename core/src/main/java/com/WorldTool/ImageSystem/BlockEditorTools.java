package com.WorldTool.ImageSystem;

import com.badlogic.gdx.math.Vector2;

public class BlockEditorTools {

    private final ImageEditorTools editingTools;

    private int[][] topTex;
    private int[][] sideTex;
    private int[][] bottomTex;

    private final Vector2 topPosition = new Vector2(50, 400); // Adjust Y as needed
    private final Vector2 sidePosition = new Vector2(150, 400); // X offset by +100
    private final Vector2 bottomPosition = new Vector2(250, 400); // X offset by +100 again

    public BlockEditorTools() {
        this.editingTools = new ImageEditorTools();
    }

    public void SetImages(int[][] tI, int[][] sI, int[][] bI) {
        topTex = tI;
        sideTex = sI;
        bottomTex = bI;
    }

    public void drawAllImages(boolean handleInput) {
        if (topTex != null) {
            editingTools.drawEditableImage(topTex, topPosition, handleInput);
        }

        if (sideTex != null) {
            editingTools.drawEditableImage(sideTex, sidePosition, handleInput);
        }

        if (bottomTex != null) {
            editingTools.drawEditableImage(bottomTex, bottomPosition, handleInput);
        }
    }
}
