package com.WorldTool.ImageSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ImageEditorTools {

    private final SpriteBatch localBatch;

    private Texture cachedTexture = null;
    private int[][] lastData = null;
    private int lastWidth = 0;
    private int lastHeight = 0;

    private final OrthographicCamera camera;
    private float zoom = 1f;

    private boolean dragging = false;
    private Vector2 dragStart = new Vector2();

    public ImageEditorTools() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        camera.update();

        localBatch = new SpriteBatch(); // Own SpriteBatch
    }

    /**
     * Updates the internal camera based on user input (zoom + pan).
     */
    private void updateCamera() {
        // Handle zoom (mouse scroll)
        int scroll = 0;
        if (scroll != 0) {
            zoom += scroll * 0.1f;
            zoom = Math.max(0.1f, Math.min(zoom, 10f));
            camera.zoom = zoom;
            camera.update();
        }

        // Handle panning (left mouse drag)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            dragging = true;
            dragStart.set(Gdx.input.getX(), Gdx.input.getY());
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            dragging = false;
        }

        if (dragging) {
            Vector2 current = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 delta = new Vector2(current).sub(dragStart);

            camera.translate(-delta.x * camera.zoom, delta.y * camera.zoom);
            camera.update();

            dragStart.set(current);
        }
    }

    /**
     * Draws an editable image to the screen using an internally managed camera.
     */
    public void drawEditableImage(int[][] argbData, Vector2 position, boolean handleInput) {
        updateCamera();
        camera.update();
        localBatch.setProjectionMatrix(camera.combined);

        int height = argbData.length;
        int width = argbData[0].length;

        // Rebuild texture if changed
        if (argbData != lastData || width != lastWidth || height != lastHeight) {
            rebuildTexture(argbData);
            lastData = argbData;
            lastWidth = width;
            lastHeight = height;
        }

        // Draw the image
        if (cachedTexture != null) {
            localBatch.begin(); // Start drawing
            localBatch.draw(cachedTexture, position.x, position.y);
            localBatch.end(); // End drawing
        }
        // Handle paint input (red pixel draw)
        if (handleInput && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            int px = (int) (worldCoords.x - position.x);
            int py = (int) (worldCoords.y - position.y);

            // Flip Y to match image coords if needed
            if (px >= 0 && py >= 0 && px < width && py < height) {
                argbData[py][px] = 0xFFFF0000; // red with full alpha
                rebuildTexture(argbData);
            }
        }
    }

    /**
     * Rebuilds the internal Texture from ARGB data.
     */
    private void rebuildTexture(int[][] argbData) {
        int height = argbData.length;
        int width = argbData[0].length;

        if (cachedTexture != null) {
            cachedTexture.dispose();
        }

        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = argbData[y][x];
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb) & 0xff;

                int rgba = (a << 24) | (r << 16) | (g << 8) | b;
                pixmap.drawPixel(x, y, rgba);
            }
        }

        cachedTexture = new Texture(pixmap);
        pixmap.dispose();
    }
}
