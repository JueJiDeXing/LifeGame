package com.jjdx.lifegame.jar.image4j.codec.ico;

import com.jjdx.lifegame.jar.image4j.io.LittleEndianInputStream;

import java.io.IOException;

public class IconEntry {
    public int bWidth;
    public int bHeight;
    public int bColorCount;
    public byte bReserved;
    public short sPlanes;
    public short sBitCount;
    public int iSizeInBytes;
    public int iFileOffset;

    public IconEntry(LittleEndianInputStream var1) throws IOException {
        this.bWidth = var1.readUnsignedByte();
        this.bHeight = var1.readUnsignedByte();
        this.bColorCount = var1.readUnsignedByte();
        this.bReserved = var1.readByte();
        this.sPlanes = var1.readShortLE();
        this.sBitCount = var1.readShortLE();
        this.iSizeInBytes = var1.readIntLE();
        this.iFileOffset = var1.readIntLE();
    }


    public String toString() {
        return "width=" +
                this.bWidth +
                ",height=" +
                this.bHeight +
                ",bitCount=" +
                this.sBitCount +
                ",colorCount=" + this.bColorCount;
    }


}
