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

    private int drawnCount = 0;
    private final float fixedScreenSpacing = 50f;

    private final OrthographicCamera camera;

    private boolean dragging = false;
    private final Vector2 dragStart = new Vector2();
    private float pixelSize = 20.0f; // World units per image pixel

    public ImageEditorTools() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        camera.update();

        localBatch = new SpriteBatch();
    }

    public void setZoom(float newZoom) {
        // Clamp pixel size (not camera.zoom)
        pixelSize = Math.max(1f, Math.min(newZoom, 100f));
    }

    public float getZoom() {
        return pixelSize;
    }

    public void render(float delta, boolean isDragging) {
        if (!isDragging)
            updateCamera();

        // Resize-aware camera update
        if (camera.viewportWidth != Gdx.graphics.getWidth() || camera.viewportHeight != Gdx.graphics.getHeight()) {
            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        camera.update();
        localBatch.setProjectionMatrix(camera.combined);
        resetDrawnIds();
    }

    public void resetDrawnIds() {
        drawnIds.clear();
        drawnCount = 0;
    }

    private void updateCamera() {
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS) || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
            setZoom(pixelSize + 1f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
            setZoom(pixelSize - 1f);
    }

    public int[][] drawEditableImage(int id, int[][] inputData, boolean handleInput, int inputColor) {
        if (!sharedPixelData.containsKey(id)) {
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

        float imageWorldWidth = width * pixelSize;
        float spacingWorld = fixedScreenSpacing * camera.zoom;

        float drawX = drawnCount * (imageWorldWidth + spacingWorld);
        float drawY = 0;

        if (handleInput && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int px = (int) ((worldCoords.x - drawX) / pixelSize);
            int py = (int) ((worldCoords.y - drawY) / pixelSize);

            if (px >= 0 && py >= 0 && px < width && py < height) {
                pixels[height - 1 - py][px] = inputColor;
                rebuildTextureForId(id, pixels);
            }
        }

        if (!drawnIds.contains(id)) {
            Texture tex = cachedTextures.get(id);
            if (tex != null) {
                float drawWidth = tex.getWidth() * pixelSize;
                float drawHeight = tex.getHeight() * pixelSize;

                localBatch.begin();
                localBatch.draw(tex, drawX, drawY, drawWidth, drawHeight);
                localBatch.end();
            }
            drawnIds.add(id);
            drawnCount++;
        }

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
