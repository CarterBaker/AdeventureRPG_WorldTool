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
        int startX = Math.max(0, (int) (camera.position.x / tileSize - viewport.getWorldWidth() / (2 * tileSize)) - 1);
        int endX = Math.min(tiles[0].length - 1,
                (int) (camera.position.x / tileSize + viewport.getWorldWidth() / (2 * tileSize)) + 1);

        int startY = Math.max(0, (int) (camera.position.y / tileSize - viewport.getWorldHeight() / (2 * tileSize)) - 1);
        int endY = Math.min(tiles.length - 1,
                (int) (camera.position.y / tileSize + viewport.getWorldHeight() / (2 * tileSize)) + 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                WorldTile tile = tiles[y][x];
                Region region = regionMap.get(tile.regionID);
                if (region == null)
                    continue;

                Color color = getTileColor(tile);
                shapeRenderer.setColor(color);
                shapeRenderer.rect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
        shapeRenderer.end();

        // --- Draw Roads and Rivers Connections ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                WorldTile tile = tiles[y][x];
                float centerX = x * tileSize + tileSize / 2f;
                float centerY = y * tileSize + tileSize / 2f;

                // → Right
                if (x + 1 < tiles[0].length) {
                    WorldTile right = tiles[y][x + 1];
                    if (tile.road && right.road) {
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.line(centerX, centerY, centerX + tileSize, centerY);
                    }
                    if (tile.river && right.river) {
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.line(centerX, centerY, centerX + tileSize, centerY);
                    }
                }

                // ↑ Up
                if (y + 1 < tiles.length) {
                    WorldTile up = tiles[y + 1][x];
                    if (tile.road && up.road) {
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.line(centerX, centerY, centerX, centerY + tileSize);
                    }
                    if (tile.river && up.river) {
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.line(centerX, centerY, centerX, centerY + tileSize);
                    }
                }

                // ↗ Up-Right
                if (x + 1 < tiles[0].length && y + 1 < tiles.length) {
                    WorldTile upRight = tiles[y + 1][x + 1];
                    if (tile.road && upRight.road) {
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.line(centerX, centerY, centerX + tileSize, centerY + tileSize);
                    }
                    if (tile.river && upRight.river) {
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.line(centerX, centerY, centerX + tileSize, centerY + tileSize);
                    }
                }

                // ↖ Up-Left
                if (x - 1 >= 0 && y + 1 < tiles.length) {
                    WorldTile upLeft = tiles[y + 1][x - 1];
                    if (tile.road && upLeft.road) {
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.line(centerX, centerY, centerX - tileSize, centerY + tileSize);
                    }
                    if (tile.river && upLeft.river) {
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.line(centerX, centerY, centerX - tileSize, centerY + tileSize);
                    }
                }
            }
        }

        shapeRenderer.end();

        // Draw grid lines only for visible tiles
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        for (int y = startY; y <= endY + 1; y++) {
            float yPos = y * tileSize;
            shapeRenderer.line(startX * tileSize, yPos, (endX + 1) * tileSize, yPos);
        }

        for (int x = startX; x <= endX + 1; x++) {
            float xPos = x * tileSize;
            shapeRenderer.line(xPos, startY * tileSize, xPos, (endY + 1) * tileSize);
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

            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

            int tileX = (int) (worldCoords.x / tileSize);
            int tileY = (int) (worldCoords.y / tileSize);

            if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
                int button = Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.RIGHT)
                        ? com.badlogic.gdx.Input.Buttons.RIGHT
                        : com.badlogic.gdx.Input.Buttons.LEFT;

                WorldTile oldTile = tiles[tileY][tileX];
                WorldTile newTile = applyToolToTile(oldTile, button);
                tiles[tileY][tileX] = newTile;
            }
        }
    }

    private WorldTile applyToolToTile(WorldTile tile, int button) {
        switch (currentTool) {
            case regionBrush:
                return applyRegionBrush(tile);
            case HeightBrush:
                return applyHeightBrush(tile, button);
            case TempermentBrush:
                return applyTemperatureBrush(tile, button);
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

    private WorldTile applyHeightBrush(WorldTile tile, int button) {
        float delta = 0.1f;
        if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
            tile.elevation -= delta;
        } else {
            tile.elevation += delta;
        }
        return tile;
    }

    private WorldTile applyTemperatureBrush(WorldTile tile, int button) {
        float delta = 1.0f;
        if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
            tile.temperature -= delta;
        } else {
            tile.temperature += delta;
        }
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

    private Color getTileColor(WorldTile tile) {
        switch (currentTool) {
            case regionBrush:
            case RiverBrush:
            case Roadbrush:
                Region region = regionMap.get(tile.regionID);
                return (region != null) ? intToColor(region.color) : Color.GRAY;

            case TempermentBrush:
                // Temperature gradient: Blue (cold) → Green → Yellow → Orange → Red (hot)
                return temperatureToColor(tile.temperature);

            case HeightBrush:
                // Height gradient: Black (low) → White (high)
                return heightToColor(tile.elevation);

            default:
                return Color.GRAY;
        }
    }

    private Color temperatureToColor(float temperature) {
        float minTemp = -30f;
        float maxTemp = 50f;

        float t = Math.max(0, Math.min(1, (temperature - minTemp) / (maxTemp - minTemp)));

        if (t < 0.25f) {
            // Blue to Green
            return interpolateColor(Color.BLUE, Color.GREEN, t / 0.25f);
        } else if (t < 0.5f) {
            // Green to Yellow
            return interpolateColor(Color.GREEN, Color.YELLOW, (t - 0.25f) / 0.25f);
        } else if (t < 0.75f) {
            // Yellow to Orange
            return interpolateColor(Color.YELLOW, Color.ORANGE, (t - 0.5f) / 0.25f);
        } else {
            // Orange to Red
            return interpolateColor(Color.ORANGE, Color.RED, (t - 0.75f) / 0.25f);
        }
    }

    private Color heightToColor(float elevation) {
        float minElev = 0f;
        float maxElev = 10f; // Adjust if necessary
        float t = Math.max(0, Math.min(1, (elevation - minElev) / (maxElev - minElev)));
        return interpolateColor(Color.BLACK, Color.WHITE, t);
    }

    private Color interpolateColor(Color start, Color end, float t) {
        float r = start.r + t * (end.r - start.r);
        float g = start.g + t * (end.g - start.g);
        float b = start.b + t * (end.b - start.b);
        float a = start.a + t * (end.a - start.a);
        return new Color(r, g, b, a);
    }

}
