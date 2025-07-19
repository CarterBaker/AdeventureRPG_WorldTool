package com.WorldTool.DisplaySystem;

import com.WorldTool.Region;
import com.WorldTool.WorldTile;
import com.WorldTool.DisplaySystem.EditorTools.WorldToolType;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;

import java.util.Map;

public class WorldEditor implements Editor {

    private WorldToolType currentTool = WorldToolType.regionBrush;

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

        handleClick();

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

        // --- Draw Gridlines ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK); // Gridline color

        int gridHeight = tiles.length;
        int gridWidth = tiles[0].length;

        for (int y = 0; y <= gridHeight; y++) {
            float yPos = y * tileSize;
            shapeRenderer.line(0, yPos, gridWidth * tileSize, yPos); // Horizontal lines
        }

        for (int x = 0; x <= gridWidth; x++) {
            float xPos = x * tileSize;
            shapeRenderer.line(xPos, 0, xPos, gridHeight * tileSize); // Vertical lines
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        float aspect = (float) width / height;
        float worldHeight = 100; // or any constant
        float worldWidth = worldHeight * aspect;

        viewport.setWorldSize(worldWidth, worldHeight);
        viewport.update(width, height, true);
    }

    private Color intToColor(int rgba) {
        float a = ((rgba >> 24) & 0xff) / 255f;
        float r = ((rgba >> 16) & 0xff) / 255f;
        float g = ((rgba >> 8) & 0xff) / 255f;
        float b = (rgba & 0xff) / 255f;
        return new Color(r, g, b, a);
    }

    public void SetToolType(WorldToolType input) {
        currentTool = input;
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

    private void handleClick() {
        if (Gdx.input.justTouched()) {
            float screenX = Gdx.input.getX();
            float screenY = Gdx.input.getY();

            // Convert screen to world coordinates
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

            int tileX = (int) (worldCoords.x / tileSize);
            int tileY = (int) (worldCoords.y / tileSize);

            if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
                WorldTile oldTile = tiles[tileY][tileX];
                WorldTile newTile = applyToolToTile(oldTile);
                tiles[tileY][tileX] = newTile;
            }
        }
    }

    private WorldTile applyToolToTile(WorldTile tile) {
        switch (currentTool) {
            case regionBrush:
                return applyRegionBrush(tile);
            case HeightBrush:
                return applyHeightBrush(tile);
            case TempermentBrush:
                return applyTemperatureBrush(tile);
            case RiverBrush:
                return applyRiverBrush(tile);
            case Roadbrush:
                return applyRoadBrush(tile);
            default:
                return tile;
        }
    }

    private WorldTile applyRegionBrush(WorldTile tile) {
        tile.regionID = currentRegion != null ? currentRegion.ID : tile.regionID;
        return tile;
    }

    private WorldTile applyHeightBrush(WorldTile tile) {
        tile.elevation += 0.1f;
        return tile;
    }

    private WorldTile applyTemperatureBrush(WorldTile tile) {
        tile.temperature += 1.0f;
        return tile;
    }

    private WorldTile applyRiverBrush(WorldTile tile) {
        tile.river = !tile.river;
        return tile;
    }

    private WorldTile applyRoadBrush(WorldTile tile) {
        tile.road = !tile.road;
        return tile;
    }

    // Get current world tiles
    public WorldTile[][] GetCurrentWorldTiles() {
        return tiles;
    }

    // Set world tiles after loading
    public void SetWorldTiles(WorldTile[][] input) {
        tiles = input;
    }

}
