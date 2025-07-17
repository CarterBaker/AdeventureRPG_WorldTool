package com.WorldTool.SaveSystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.WorldTool.Block;

public class SaveBlocks {

    private static final String BlockFile = "blocks.dat";
    private final File saveFile;

    public SaveBlocks(String path) {
        // Create the directory if it doesn't exist
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // Define the full path to the blocks.dat file
        saveFile = new File(folder, BlockFile);
    }

    public void Save(Block block) {
        Map<Integer, Block> blocks = LoadAll();
        blocks.put(block.ID, block);

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)))) {
            for (Block b : blocks.values()) {
                dos.writeInt(b.ID);
                dos.writeInt(b.top);
                dos.writeInt(b.bottom);
                dos.writeInt(b.sides);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Block Load(int ID) {
        if (!saveFile.exists()) {
            return null;
        }

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(saveFile)))) {
            while (dis.available() >= 16) {
                int id = dis.readInt();
                int top = dis.readInt();
                int bottom = dis.readInt();
                int sides = dis.readInt();

                if (id == ID) {
                    Block block = new Block();
                    block.ID = id;
                    block.top = top;
                    block.bottom = bottom;
                    block.sides = sides;
                    return block;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, Block> LoadAll() {
        Map<Integer, Block> blocks = new HashMap<>();
        if (!saveFile.exists())
            return blocks;

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(saveFile)))) {
            while (dis.available() >= 16) {
                Block block = new Block();
                block.ID = dis.readInt();
                block.top = dis.readInt();
                block.bottom = dis.readInt();
                block.sides = dis.readInt();
                blocks.put(block.ID, block);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return blocks;
    }
}
