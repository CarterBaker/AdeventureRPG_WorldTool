package com.WorldTool.SaveSystem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.WorldTool.Region;

public class SaveRegions {

    private static final String RegionFile = "regions.dat";
    private final File saveFile;

    public SaveRegions(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        saveFile = new File(folder, RegionFile);
    }

    public void Save(Region region) {
        Map<Integer, Region> regions = LoadAll();
        regions.put(region.ID, region);

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)))) {
            for (Region r : regions.values()) {
                dos.writeInt(r.ID);

                byte[] nameBytes = r.name.getBytes("UTF-8");
                dos.writeInt(nameBytes.length);
                dos.write(nameBytes);

                dos.writeInt(r.color); // Store color as-is (assume already little-endian)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Region Load(int ID) {
        if (!saveFile.exists()) return null;

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(saveFile)))) {
            while (dis.available() > 0) {
                int id = dis.readInt();

                int nameLength = dis.readInt();
                byte[] nameBytes = new byte[nameLength];
                dis.readFully(nameBytes);
                String name = new String(nameBytes, "UTF-8");

                int color = dis.readInt();

                if (id == ID) {
                    Region r = new Region();
                    r.ID = id;
                    r.name = name;
                    r.color = color;
                    return r;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<Integer, Region> LoadAll() {
        Map<Integer, Region> regions = new HashMap<>();
        if (!saveFile.exists()) return regions;

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(saveFile)))) {
            while (dis.available() > 0) {
                Region r = new Region();
                r.ID = dis.readInt();

                int nameLength = dis.readInt();
                byte[] nameBytes = new byte[nameLength];
                dis.readFully(nameBytes);
                r.name = new String(nameBytes, "UTF-8");

                r.color = dis.readInt();
                regions.put(r.ID, r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return regions;
    }
}
