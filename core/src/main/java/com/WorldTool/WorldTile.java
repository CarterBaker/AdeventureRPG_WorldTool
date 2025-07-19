package com.WorldTool;

public class WorldTile {
    public int regionID;
    public float elevation;
    public float temperature;
    public boolean road;
    public boolean river;

    public WorldTile(int regionID, float elevation, float temperature, boolean road, boolean river) {
        this.regionID = regionID;
        this.elevation = elevation;
        this.temperature = temperature;
        this.road = road;
        this.river = river;
    }

    public WorldTile() {} // Optional default constructor if you want to initialize later
}
