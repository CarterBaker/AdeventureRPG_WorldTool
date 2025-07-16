package com.WorldTool.ImageSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private final Map<Integer, int[][]> sharedPixelData = new HashMap<>();
    private final Map<Integer, Texture> cachedTextures = new HashMap<>();
    private final Set<Integer> drawnIds = new HashSet<>();

    private final OrthographicCamera camera;

    private boolean dragging = false;
    private final Vector2 dragStart = new Vector2();
    private float zoom = 1.0f; // Zoom factor

    public ImageEditorTools() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        camera.update();

        localBatch = new SpriteBatch(); // Own SpriteBatch
    }

    public void setZoom(float newZoom) {
        // Clamp zoom level between 0.1 and 20
        zoom = Math.max(0.1f, Math.min(newZoom, 20f));
    }

    public float getZoom() {
        return zoom;
    }

    public void render(float delta, boolean isDragging) {
        if (!isDragging)
            updateCamera();

        camera.update();
        localBatch.setProjectionMatrix(camera.combined);

        resetDrawnIds();
    }

    public void resetDrawnIds() {
        drawnIds.clear();
    }

    private void updateCamera() {
        // Existing panning logic (unchanged)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            dragging = true;
            dragStart.set(Gdx.input.getX(), Gdx.input.getY());
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT))
            dragging = false;

        if (dragging) {
            Vector2 current = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 delta = new Vector2(current).sub(dragStart);

            camera.translate(-delta.x * camera.zoom, delta.y * camera.zoom);
            camera.update();

            dragStart.set(current);
        }

        // Optional: Zoom with keyboard for demo
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS) || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
            setZoom(zoom + 0.1f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
            setZoom(zoom - 0.1f);
    }

    public int[][] drawEditableImage(int id, int[][] inputData, Vector2 position, boolean handleInput, int inputColor) {
        // Initialize shared pixel data if not present
        if (!sharedPixelData.containsKey(id)) {
            // Copy inputData deeply to avoid aliasing
            int height = inputData.length;
            int width = inputData[0].length;
            int[][] copy = new int[height][width];
            for (int y = 0; y < height; y++) {
                System.arraycopy(inputData[y], 0, copy[y], 0, width);
            }
            sharedPixelData.put(id, copy);
            rebuildTextureForId(id, copy);
        }

        int[][] pixels = sharedPixelData.get(id);
        int height = pixels.length;
        int width = pixels[0].length;

        // Handle painting input on shared pixel data
        if (handleInput && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int px = (int) ((worldCoords.x - position.x) / zoom);
            int py = (int) ((worldCoords.y - position.y) / zoom);

            if (px >= 0 && py >= 0 && px < width && py < height) {
                pixels[height - 1 - py][px] = inputColor;
                rebuildTextureForId(id, pixels);
            }
        }

        // Draw texture once per ID per frame
        if (!drawnIds.contains(id)) {
            Texture tex = cachedTextures.get(id);
            if (tex != null) {
                localBatch.begin();
                localBatch.draw(tex, position.x, position.y, tex.getWidth() * zoom, tex.getHeight() * zoom);
                localBatch.end();
            }
            drawnIds.add(id);
        }

        // Return the shared pixel data for this ID
        return pixels;
    }

    private void rebuildTextureForId(int id, int[][] pixels) {
        Texture old = cachedTextures.get(id);
        if (old != null)
            old.dispose();

        int height = pixels.length;
        int width = pixels[0].length;

        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixels[y][x];
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = argb & 0xff;
                int a = (argb >> 24) & 0xff;
                int rgba = (r << 24) | (g << 16) | (b << 8) | a;
                pixmap.drawPixel(x, y, rgba);
            }
        }

        cachedTextures.put(id, new Texture(pixmap));
        pixmap.dispose();
    }

}
