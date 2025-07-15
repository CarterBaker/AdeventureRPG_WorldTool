package com.WorldTool.SaveSystem;

import com.WorldTool.Block;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SaveBlocks {

    private String path;

    public SaveBlocks(String path) {
        this.path = path;
    }

    public void Save(Block block) {
        Map<Integer, Block> blocks = LoadAll();
        blocks.put(block.ID, block);

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path)))) {
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
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            while (dis.available() >= 16) { // 4 ints = 16 bytes
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
        File file = new File(path);
        if (!file.exists()) return blocks;

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
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
