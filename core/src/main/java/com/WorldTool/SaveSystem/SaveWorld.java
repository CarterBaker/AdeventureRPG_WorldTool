package com.WorldTool.SaveSystem;

import com.WorldTool.WorldTile;

import java.io.*;

public class SaveWorld {

    private static final String WorldFile = "world.dat";
    private final File saveFile;

    public SaveWorld(String path) {
        File folder = new File(path);
        if (!folder.exists()) folder.mkdirs();
        this.saveFile = new File(folder, WorldFile);
    }

    public void Save(WorldTile[][] world) {
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)))) {
            int width = world.length;
            int height = world[0].length;

            dos.writeInt(width);  // store grid size
            dos.writeInt(height);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    WorldTile tile = world[x][y];

                    dos.writeInt(tile.regionID);          // 4 bytes
                    dos.writeFloat(tile.elevation);       // 4 bytes
                    dos.writeFloat(tile.temperature);     // 4 bytes
                    dos.writeByte(tile.road ? 1 : 0);      // 1 byte
                    dos.writeByte(tile.river ? 1 : 0);     // 1 byte
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WorldTile[][] Load() {
        if (!saveFile.exists()) return null;

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(saveFile)))) {
            int width = dis.readInt();
            int height = dis.readInt();

            WorldTile[][] world = new WorldTile[width][height];

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int regionID = dis.readInt();
                    float elevation = dis.readFloat();
                    float temperature = dis.readFloat();
                    boolean road = dis.readByte() == 1;
                    boolean river = dis.readByte() == 1;

                    world[x][y] = new WorldTile(regionID, elevation, temperature, road, river);
                }
            }

            return world;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
