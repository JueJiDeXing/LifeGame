//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.codec.bmp;

import com.jjdx.lifegame.jar.image4j.io.LittleEndianInputStream;
import com.jjdx.lifegame.jar.image4j.io.LittleEndianOutputStream;

import java.io.IOException;

public class InfoHeader {
    public int iSize;
    public int iWidth;
    public int iHeight;
    public short sPlanes;
    public short sBitCount;
    public int iCompression;
    public int iImageSize;
    public int iXpixelsPerM;
    public int iYpixelsPerM;
    public int iColorsUsed;
    public int iColorsImportant;
    public int iNumColors;

    public InfoHeader(LittleEndianInputStream var1) throws IOException {
        this.iSize = var1.readIntLE();
        this.init(var1, this.iSize);
    }

    public InfoHeader(LittleEndianInputStream var1, int var2) throws IOException {
        this.init(var1, var2);
    }

    protected void init(LittleEndianInputStream var1, int var2) throws IOException {
        this.iSize = var2;
        this.iWidth = var1.readIntLE();
        this.iHeight = var1.readIntLE();
        this.sPlanes = var1.readShortLE();
        this.sBitCount = var1.readShortLE();
        this.iNumColors = (int)Math.pow(2.0, (double)this.sBitCount);
        this.iCompression = var1.readIntLE();
        this.iImageSize = var1.readIntLE();
        this.iXpixelsPerM = var1.readIntLE();
        this.iYpixelsPerM = var1.readIntLE();
        this.iColorsUsed = var1.readIntLE();
        this.iColorsImportant = var1.readIntLE();
    }

    public InfoHeader() {
        this.iSize = 40;
        this.iWidth = 0;
        this.iHeight = 0;
        this.sPlanes = 1;
        this.sBitCount = 0;
        this.iNumColors = 0;
        this.iCompression = 0;
        this.iImageSize = 0;
        this.iXpixelsPerM = 0;
        this.iYpixelsPerM = 0;
        this.iColorsUsed = 0;
        this.iColorsImportant = 0;
    }

    public InfoHeader(InfoHeader var1) {
        this.iColorsImportant = var1.iColorsImportant;
        this.iColorsUsed = var1.iColorsUsed;
        this.iCompression = var1.iCompression;
        this.iHeight = var1.iHeight;
        this.iWidth = var1.iWidth;
        this.iImageSize = var1.iImageSize;
        this.iNumColors = var1.iNumColors;
        this.iSize = var1.iSize;
        this.iXpixelsPerM = var1.iXpixelsPerM;
        this.iYpixelsPerM = var1.iYpixelsPerM;
        this.sBitCount = var1.sBitCount;
        this.sPlanes = var1.sPlanes;
    }

    public void write(LittleEndianOutputStream var1) throws IOException {
        var1.writeIntLE(this.iSize);
        var1.writeIntLE(this.iWidth);
        var1.writeIntLE(this.iHeight);
        var1.writeShortLE(this.sPlanes);
        var1.writeShortLE(this.sBitCount);
        var1.writeIntLE(this.iCompression);
        var1.writeIntLE(this.iImageSize);
        var1.writeIntLE(this.iXpixelsPerM);
        var1.writeIntLE(this.iYpixelsPerM);
        var1.writeIntLE(this.iColorsUsed);
        var1.writeIntLE(this.iColorsImportant);
    }
}
