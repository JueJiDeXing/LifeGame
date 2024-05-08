//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.codec.ico;

import com.jjdx.lifegame.jar.image4j.io.LittleEndianInputStream;
import com.jjdx.lifegame.jar.image4j.io.LittleEndianOutputStream;

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

    public IconEntry() {
        this.bWidth = 0;
        this.bHeight = 0;
        this.bColorCount = 0;
        this.sPlanes = 1;
        this.bReserved = 0;
        this.sBitCount = 0;
        this.iSizeInBytes = 0;
        this.iFileOffset = 0;
    }

    public String toString() {
        StringBuffer var1 = new StringBuffer();
        var1.append("width=");
        var1.append(this.bWidth);
        var1.append(",height=");
        var1.append(this.bHeight);
        var1.append(",bitCount=");
        var1.append(this.sBitCount);
        var1.append(",colorCount=" + this.bColorCount);
        return var1.toString();
    }

    public void write(LittleEndianOutputStream var1) throws IOException {
        var1.writeByte(this.bWidth);
        var1.writeByte(this.bHeight);
        var1.writeByte(this.bColorCount);
        var1.writeByte(this.bReserved);
        var1.writeShortLE(this.sPlanes);
        var1.writeShortLE(this.sBitCount);
        var1.writeIntLE(this.iSizeInBytes);
        var1.writeIntLE(this.iFileOffset);
    }
}
