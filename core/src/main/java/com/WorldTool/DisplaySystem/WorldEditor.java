package com.WorldTool.DisplaySystem;

import com.WorldTool.Region;
import com.WorldTool.WorldTile;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;

import java.util.Map;

public class WorldEditor implements Editor {

    private WorldTile[][] tiles;
    Map<Integer, Region> regionMap;
    Region currentRegion;

    private ShapeRenderer shapeRenderer;
    private int tileSize = 8;

    private OrthographicCamera camera;
    private FitViewport viewport;

    private float zoomSpeed = 0.1f;
    private float dragSpeed = 1.0f;
    private float minZoom = 0.5f;
    private float maxZoom = 10f;

    private float lastX, lastY;
    private boolean dragging = false;

    public WorldEditor(Map<Integer, Region> regionMap) {
        this.regionMap = regionMap;
        this.shapeRenderer = new ShapeRenderer();

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(800, 600, camera); // You can customize this size
        this.viewport.apply();
        this.camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        this.camera.update();
    }

    public void loadTiles(WorldTile[][] inputTiles) {
        this.tiles = inputTiles;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (tiles == null)
            return;

        // --- Handle Zoom ---
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.PLUS)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.EQUALS)) {
            camera.zoom = Math.max(minZoom, camera.zoom - zoomSpeed);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.MINUS)) {
            camera.zoom = Math.min(maxZoom, camera.zoom + zoomSpeed);
        }

        // --- Handle Mouse Drag ---
        if (Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
            float screenX = Gdx.input.getX();
            float screenY = Gdx.input.getY();

            if (!dragging) {
                lastX = screenX;
                lastY = screenY;
                dragging = true;
            } else {
                float deltaX = (screenX - lastX) * camera.zoom * dragSpeed;
                float deltaY = (lastY - screenY) * camera.zoom * dragSpeed;
                camera.position.add(-deltaX, -deltaY, 0);
                lastX = screenX;
                lastY = screenY;
            }
        } else {
            dragging = false;
        }

        // --- Camera Update ---
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        // --- Draw Tiles ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                WorldTile tile = tiles[y][x];
                Region region = regionMap.get(tile.regionID);
                if (region == null)
                    continue;

                Color color = intToColor(region.color);
                shapeRenderer.setColor(color);
                shapeRenderer.rect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private Color intToColor(int rgba) {
        float a = ((rgba >> 24) & 0xff) / 255f;
        float r = ((rgba >> 16) & 0xff) / 255f;
        float g = ((rgba >> 8) & 0xff) / 255f;
        float b = (rgba & 0xff) / 255f;
        return new Color(r, g, b, a);
    }

    public void SetWorldScale(int newWidth, int newHeight) {
        if (tiles == null) {
            tiles = new WorldTile[newHeight][newWidth];
            fillWithDefaultTiles(tiles, 0, 0);
            return;
        }

        int currentHeight = tiles.length;
        int currentWidth = tiles[0].length;

        WorldTile[][] newTiles = new WorldTile[newHeight][newWidth];

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                if (y < currentHeight && x < currentWidth) {
                    newTiles[y][x] = tiles[y][x];
                } else {
                    newTiles[y][x] = new WorldTile();
                }
            }
        }

        tiles = newTiles;
    }

    private void fillWithDefaultTiles(WorldTile[][] grid, int startX, int startY) {
        for (int y = startY; y < grid.length; y++) {
            for (int x = startX; x < grid[0].length; x++) {
                grid[y][x] = new WorldTile();
            }
        }
    }

    public void SetCurrentRegion(Region region) {
        currentRegion = region;
    }
}
