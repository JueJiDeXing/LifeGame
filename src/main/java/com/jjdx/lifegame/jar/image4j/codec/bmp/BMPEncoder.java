//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.codec.bmp;

import com.jjdx.lifegame.jar.image4j.io.LittleEndianOutputStream;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BMPEncoder {
    private BMPEncoder() {
    }

    public static void write(BufferedImage var0, File var1) throws IOException {
        FileOutputStream var2 = new FileOutputStream(var1);

        try {
            BufferedOutputStream var3 = new BufferedOutputStream(var2);
            write(var0, (OutputStream)var3);
            var3.flush();
        } finally {
            try {
                var2.close();
            } catch (IOException var9) {
            }

        }

    }

    public static void write(BufferedImage var0, OutputStream var1) throws IOException {
        InfoHeader var2 = createInfoHeader(var0);
        int var3 = 0;
        IndexColorModel var4 = null;
        if (var2.sBitCount <= 8) {
            var4 = (IndexColorModel)var0.getColorModel();
            var3 = var4.getMapSize();
        }

        int var5 = 14 + var2.iSize;
        int var6 = 4 * var3;
        int var7 = var5 + var6;
        int var8 = 0;
        switch (var2.sBitCount) {
            case 1:
                var8 = getBytesPerLine1(var2.iWidth);
                break;
            case 4:
                var8 = getBytesPerLine4(var2.iWidth);
                break;
            case 8:
                var8 = getBytesPerLine8(var2.iWidth);
                break;
            case 24:
                var8 = getBytesPerLine24(var2.iWidth);
                break;
            case 32:
                var8 = var2.iWidth * 4;
        }

        int var9 = var7 + var8 * var2.iHeight;
        LittleEndianOutputStream var10 = new LittleEndianOutputStream(var1);
        writeFileHeader(var9, var7, var10);
        var2.write(var10);
        if (var2.sBitCount <= 8) {
            writeColorMap(var4, var10);
        }

        switch (var2.sBitCount) {
            case 1:
                write1(var0.getRaster(), var10);
                break;
            case 4:
                write4(var0.getRaster(), var10);
                break;
            case 8:
                write8(var0.getRaster(), var10);
                break;
            case 24:
                write24(var0.getRaster(), var10);
                break;
            case 32:
                write32(var0.getRaster(), var0.getAlphaRaster(), var10);
        }

    }

    public static InfoHeader createInfoHeader(BufferedImage var0) {
        InfoHeader var1 = new InfoHeader();
        var1.iColorsImportant = 0;
        var1.iColorsUsed = 0;
        var1.iCompression = 0;
        var1.iHeight = var0.getHeight();
        var1.iWidth = var0.getWidth();
        var1.sBitCount = (short)var0.getColorModel().getPixelSize();
        var1.iNumColors = 1 << (var1.sBitCount == 32 ? 24 : var1.sBitCount);
        var1.iImageSize = 0;
        return var1;
    }

    public static void writeFileHeader(int var0, int var1, LittleEndianOutputStream var2) throws IOException {
        byte[] var3 = "BM".getBytes("UTF-8");
        var2.write(var3);
        var2.writeIntLE(var0);
        var2.writeIntLE(0);
        var2.writeIntLE(var1);
    }

    public static void writeColorMap(IndexColorModel var0, LittleEndianOutputStream var1) throws IOException {
        int var2 = var0.getMapSize();

        for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = var0.getRGB(var3);
            int var5 = var4 >> 16 & 255;
            int var6 = var4 >> 8 & 255;
            int var7 = var4 & 255;
            var1.writeByte(var7);
            var1.writeByte(var6);
            var1.writeByte(var5);
            var1.writeByte(0);
        }

    }

    public static int getBytesPerLine1(int var0) {
        int var1 = var0 / 8;
        if (var1 * 8 < var0) {
            ++var1;
        }

        if (var1 % 4 != 0) {
            var1 = (var1 / 4 + 1) * 4;
        }

        return var1;
    }

    public static int getBytesPerLine4(int var0) {
        int var1 = var0 / 2;
        if (var1 % 4 != 0) {
            var1 = (var1 / 4 + 1) * 4;
        }

        return var1;
    }

    public static int getBytesPerLine8(int var0) {
        int var1 = var0;
        if (var0 % 4 != 0) {
            var1 = (var0 / 4 + 1) * 4;
        }

        return var1;
    }

    public static int getBytesPerLine24(int var0) {
        int var1 = var0 * 3;
        if (var1 % 4 != 0) {
            var1 = (var1 / 4 + 1) * 4;
        }

        return var1;
    }

    public static int getBitmapSize(int var0, int var1, int var2) {
        int var3 = 0;
        switch (var2) {
            case 1:
                var3 = getBytesPerLine1(var0);
                break;
            case 4:
                var3 = getBytesPerLine4(var0);
                break;
            case 8:
                var3 = getBytesPerLine8(var0);
                break;
            case 24:
                var3 = getBytesPerLine24(var0);
                break;
            case 32:
                var3 = var0 * 4;
        }

        int var4 = var3 * var1;
        return var4;
    }

    public static void write1(Raster var0, LittleEndianOutputStream var1) throws IOException {
        int var2 = getBytesPerLine1(var0.getWidth());
        byte[] var3 = new byte[var2];

        for(int var4 = var0.getHeight() - 1; var4 >= 0; --var4) {
            int var5;
            for(var5 = 0; var5 < var2; ++var5) {
                var3[var5] = 0;
            }

            for(var5 = 0; var5 < var0.getWidth(); ++var5) {
                int var6 = var5 / 8;
                int var7 = var5 % 8;
                int var8 = var0.getSample(var5, var4, 0);
                var3[var6] = setBit(var3[var6], var7, var8);
            }

            var1.write(var3);
        }

    }

    public static void write4(Raster var0, LittleEndianOutputStream var1) throws IOException {
        int var2 = var0.getWidth();
        int var3 = var0.getHeight();
        int var4 = getBytesPerLine4(var2);
        byte[] var5 = new byte[var4];

        for(int var6 = var3 - 1; var6 >= 0; --var6) {
            int var7;
            for(var7 = 0; var7 < var4; ++var7) {
                var5[var7] = 0;
            }

            for(var7 = 0; var7 < var2; ++var7) {
                int var8 = var7 / 2;
                int var9 = var7 % 2;
                int var10 = var0.getSample(var7, var6, 0);
                var5[var8] = setNibble(var5[var8], var9, var10);
            }

            var1.write(var5);
        }

    }

    public static void write8(Raster var0, LittleEndianOutputStream var1) throws IOException {
        int var2 = var0.getWidth();
        int var3 = var0.getHeight();
        int var4 = getBytesPerLine8(var2);

        for(int var5 = var3 - 1; var5 >= 0; --var5) {
            int var6;
            for(var6 = 0; var6 < var2; ++var6) {
                int var7 = var0.getSample(var6, var5, 0);
                var1.writeByte(var7);
            }

            for(var6 = var2; var6 < var4; ++var6) {
                var1.writeByte(0);
            }
        }

    }

    public static void write24(Raster var0, LittleEndianOutputStream var1) throws IOException {
        int var2 = var0.getWidth();
        int var3 = var0.getHeight();
        int var4 = getBytesPerLine24(var2);

        for(int var5 = var3 - 1; var5 >= 0; --var5) {
            int var6;
            for(var6 = 0; var6 < var2; ++var6) {
                int var7 = var0.getSample(var6, var5, 0);
                int var8 = var0.getSample(var6, var5, 1);
                int var9 = var0.getSample(var6, var5, 2);
                var1.writeByte(var9);
                var1.writeByte(var8);
                var1.writeByte(var7);
            }

            for(var6 = var2 * 3; var6 < var4; ++var6) {
                var1.writeByte(0);
            }
        }

    }

    public static void write32(Raster var0, Raster var1, LittleEndianOutputStream var2) throws IOException {
        int var3 = var0.getWidth();
        int var4 = var0.getHeight();

        for(int var5 = var4 - 1; var5 >= 0; --var5) {
            for(int var6 = 0; var6 < var3; ++var6) {
                int var7 = var0.getSample(var6, var5, 0);
                int var8 = var0.getSample(var6, var5, 1);
                int var9 = var0.getSample(var6, var5, 2);
                int var10 = var1.getSample(var6, var5, 0);
                var2.writeByte(var9);
                var2.writeByte(var8);
                var2.writeByte(var7);
                var2.writeByte(var10);
            }
        }

    }

    private static byte setBit(byte var0, int var1, int var2) {
        if (var2 == 0) {
            var0 = (byte)(var0 & ~(1 << 7 - var1));
        } else {
            var0 = (byte)(var0 | 1 << 7 - var1);
        }

        return var0;
    }

    private static byte setNibble(byte var0, int var1, int var2) {
        var0 = (byte)(var0 | var2 << (1 - var1) * 4);
        return var0;
    }

    public static int getColorMapSize(short var0) {
        int var1 = 0;
        if (var0 <= 8) {
            var1 = (1 << var0) * 4;
        }

        return var1;
    }
}
