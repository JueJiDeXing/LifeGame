package com.jjdx.lifegame.jar.image4j.codec.bmp;

import com.jjdx.lifegame.jar.image4j.io.LittleEndianInputStream;

import java.io.IOException;

public class ColorEntry {
    public int bRed;
    public int bGreen;
    public int bBlue;
    public int bReserved;

    public ColorEntry(LittleEndianInputStream var1) throws IOException {
        this.bBlue = var1.readUnsignedByte();
        this.bGreen = var1.readUnsignedByte();
        this.bRed = var1.readUnsignedByte();
        this.bReserved = var1.readUnsignedByte();
    }


    public ColorEntry(int var1, int var2, int var3, int var4) {
        this.bBlue = var3;
        this.bGreen = var2;
        this.bRed = var1;
        this.bReserved = var4;
    }
}
