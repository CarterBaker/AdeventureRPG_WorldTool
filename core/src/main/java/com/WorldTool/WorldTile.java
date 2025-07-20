package com.WorldTool;

public class WorldTile {
    public int regionID;
    public int elevation;
    public int temperature;
    public boolean road;
    public boolean river;

    public WorldTile(int regionID, int elevation, int temperature, boolean road, boolean river) {
        this.regionID = regionID;
        this.elevation = elevation;
        this.temperature = temperature;
        this.road = road;
        this.river = river;
    }

    public WorldTile() {} // Optional default constructor if you want to initialize later
}
