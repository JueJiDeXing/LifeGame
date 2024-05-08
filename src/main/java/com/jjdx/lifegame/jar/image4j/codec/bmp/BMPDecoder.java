package com.jjdx.lifegame.jar.image4j.codec.bmp;

import com.jjdx.lifegame.jar.image4j.io.CountingInputStream;
import com.jjdx.lifegame.jar.image4j.io.LittleEndianInputStream;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BMPDecoder {

    public BMPDecoder(InputStream var1) throws IOException {
        LittleEndianInputStream var2 = new LittleEndianInputStream(new CountingInputStream(var1));
        byte[] var3 = new byte[2];
        var2.read(var3);
        String var4 = new String(var3, StandardCharsets.UTF_8);
        if (!var4.equals("BM")) {
            throw new IOException("Invalid signature '" + var4 + "' for BMP format");
        } else {
            var2.readIntLE();
            var2.readIntLE();
            var2.readIntLE();
            InfoHeader infoHeader = readInfoHeader(var2);
            BufferedImage img = read(infoHeader, var2);
        }
    }

    private static int getBit(int var0, int var1) {
        return var0 >> 7 - var1 & 1;
    }

    private static int getNibble(int var0, int var1) {
        return var0 >> 4 * (1 - var1) & 15;
    }


    private static void getColorTable(ColorEntry[] var0, byte[] var1, byte[] var2, byte[] var3) {
        for (int var4 = 0; var4 < var0.length; ++var4) {
            var1[var4] = (byte) var0[var4].bRed;
            var2[var4] = (byte) var0[var4].bGreen;
            var3[var4] = (byte) var0[var4].bBlue;
        }

    }

    public static InfoHeader readInfoHeader(LittleEndianInputStream var0) throws IOException {
        return new InfoHeader(var0);
    }

    public static InfoHeader readInfoHeader(LittleEndianInputStream var0, int var1) throws IOException {
        return new InfoHeader(var0, var1);
    }

    public static BufferedImage read(InfoHeader var0, LittleEndianInputStream var1) throws IOException {
        BufferedImage var2;
        ColorEntry[] var3 = null;
        if (var0.sBitCount <= 8) {
            var3 = readColorTable(var0, var1);
        }
        var2 = read(var0, var1, var3);
        return var2;
    }

    public static BufferedImage read(InfoHeader var0, LittleEndianInputStream var1, ColorEntry[] var2) throws IOException {
        BufferedImage var3;
        if (var0.sBitCount == 1 && var0.iCompression == 0) {
            var3 = read1(var0, var1, var2);
        } else if (var0.sBitCount == 4 && var0.iCompression == 0) {
            var3 = read4(var0, var1, var2);
        } else if (var0.sBitCount == 8 && var0.iCompression == 0) {
            var3 = read8(var0, var1, var2);
        } else if (var0.sBitCount == 24 && var0.iCompression == 0) {
            var3 = read24(var0, var1);
        } else {
            if (var0.sBitCount != 32 || var0.iCompression != 0) {
                throw new IOException("Unrecognized bitmap format: bit count=" + var0.sBitCount + ", compression=" + var0.iCompression);
            }

            var3 = read32(var0, var1);
        }

        return var3;
    }

    public static ColorEntry[] readColorTable(InfoHeader var0, LittleEndianInputStream var1) throws IOException {
        ColorEntry[] var2 = new ColorEntry[var0.iNumColors];

        for (int var3 = 0; var3 < var0.iNumColors; ++var3) {
            ColorEntry var4 = new ColorEntry(var1);
            var2[var3] = var4;
        }

        return var2;
    }

    public static BufferedImage read1(InfoHeader var0, LittleEndianInputStream var1, ColorEntry[] var2) throws IOException {
        byte[] var3 = new byte[var2.length];
        byte[] var4 = new byte[var2.length];
        byte[] var5 = new byte[var2.length];
        getColorTable(var2, var3, var4, var5);
        IndexColorModel var6 = new IndexColorModel(1, 2, var3, var4, var5);
        BufferedImage var7 = new BufferedImage(var0.iWidth, var0.iHeight, 12, var6);
        WritableRaster var8 = var7.getRaster();
        int var9 = var0.iWidth;
        int var10 = var9;
        if (var9 % 32 != 0) {
            var10 = (var9 / 32 + 1) * 32;
        }

        int var11 = var10 - var9;
        int var13 = var10 / 8;
        int[] var14 = new int[var13];

        for (int var15 = var0.iHeight - 1; var15 >= 0; --var15) {
            int var16;
            for (var16 = 0; var16 < var13; ++var16) {
                var14[var16] = var1.readUnsignedByte();
            }

            for (var16 = 0; var16 < var0.iWidth; ++var16) {
                int var17 = var16 / 8;
                int var18 = var14[var17];
                int var19 = var16 % 8;
                int var20 = getBit(var18, var19);
                var8.setSample(var16, var15, 0, var20);
            }
        }

        return var7;
    }

    public static BufferedImage read4(InfoHeader var0, LittleEndianInputStream var1, ColorEntry[] var2) throws IOException {
        byte[] var3 = new byte[var2.length];
        byte[] var4 = new byte[var2.length];
        byte[] var5 = new byte[var2.length];
        getColorTable(var2, var3, var4, var5);
        IndexColorModel var6 = new IndexColorModel(4, var0.iNumColors, var3, var4, var5);
        BufferedImage var7 = new BufferedImage(var0.iWidth, var0.iHeight, 12, var6);
        WritableRaster var8 = var7.getRaster();
        int var9 = var0.iWidth * 4;
        if (var9 % 32 != 0) {
            var9 = (var9 / 32 + 1) * 32;
        }

        int var10 = var9 / 8;
        int[] var11 = new int[var10];

        for (int var12 = var0.iHeight - 1; var12 >= 0; --var12) {
            int var13;
            int var14;
            for (var13 = 0; var13 < var10; ++var13) {
                var14 = var1.readUnsignedByte();
                var11[var13] = var14;
            }

            for (var13 = 0; var13 < var0.iWidth; ++var13) {
                var14 = var13 / 2;
                int var15 = var13 % 2;
                int var16 = var11[var14];
                int var17 = getNibble(var16, var15);
                var8.setSample(var13, var12, 0, var17);
            }
        }

        return var7;
    }

    public static BufferedImage read8(InfoHeader var0, LittleEndianInputStream var1, ColorEntry[] var2) throws IOException {
        byte[] var3 = new byte[var2.length];
        byte[] var4 = new byte[var2.length];
        byte[] var5 = new byte[var2.length];
        getColorTable(var2, var3, var4, var5);
        IndexColorModel var6 = new IndexColorModel(8, var0.iNumColors, var3, var4, var5);
        BufferedImage var7 = new BufferedImage(var0.iWidth, var0.iHeight, 13, var6);
        WritableRaster var8 = var7.getRaster();
        int var9 = var0.iWidth;
        int var10 = var9;
        if (var9 % 4 != 0) {
            var10 = (var9 / 4 + 1) * 4;
        }

        int var11 = var10 - var9;

        for (int var12 = var0.iHeight - 1; var12 >= 0; --var12) {
            for (int var13 = 0; var13 < var0.iWidth; ++var13) {
                int var14 = var1.readUnsignedByte();
                var8.setSample(var13, var12, 0, var14);
            }

            var1.skip(var11);
        }

        return var7;
    }

    public static BufferedImage read24(InfoHeader var0, LittleEndianInputStream var1) throws IOException {
        BufferedImage var2 = new BufferedImage(var0.iWidth, var0.iHeight, 1);
        WritableRaster var3 = var2.getRaster();
        int var4 = var0.iWidth * 3;
        int var5 = var4;
        if (var4 % 4 != 0) {
            var5 = (var4 / 4 + 1) * 4;
        }

        int var6 = var5 - var4;

        for (int var7 = var0.iHeight - 1; var7 >= 0; --var7) {
            for (int var8 = 0; var8 < var0.iWidth; ++var8) {
                int var9 = var1.readUnsignedByte();
                int var10 = var1.readUnsignedByte();
                int var11 = var1.readUnsignedByte();
                var3.setSample(var8, var7, 0, var11);
                var3.setSample(var8, var7, 1, var10);
                var3.setSample(var8, var7, 2, var9);
            }

            var1.skip(var6);
        }

        return var2;
    }

    public static BufferedImage read32(InfoHeader var0, LittleEndianInputStream var1) throws IOException {
        BufferedImage var2 = new BufferedImage(var0.iWidth, var0.iHeight, 2);
        WritableRaster var3 = var2.getRaster();
        WritableRaster var4 = var2.getAlphaRaster();

        for (int var5 = var0.iHeight - 1; var5 >= 0; --var5) {
            for (int var6 = 0; var6 < var0.iWidth; ++var6) {
                int var7 = var1.readUnsignedByte();
                int var8 = var1.readUnsignedByte();
                int var9 = var1.readUnsignedByte();
                int var10 = var1.readUnsignedByte();
                var3.setSample(var6, var5, 0, var9);
                var3.setSample(var6, var5, 1, var8);
                var3.setSample(var6, var5, 2, var7);
                var4.setSample(var6, var5, 0, var10);
            }
        }

        return var2;
    }

}
