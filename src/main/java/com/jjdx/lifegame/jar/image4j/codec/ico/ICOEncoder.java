//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.codec.ico;

import com.jjdx.lifegame.jar.image4j.codec.bmp.BMPEncoder;
import com.jjdx.lifegame.jar.image4j.codec.bmp.InfoHeader;
import com.jjdx.lifegame.jar.image4j.io.LittleEndianOutputStream;
import com.jjdx.lifegame.jar.image4j.util.ConvertUtil;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ICOEncoder {
    private ICOEncoder() {
    }

    public static void write(BufferedImage var0, File var1) throws IOException {
        write(var0, -1, (File)var1);
    }

    public static void write(BufferedImage var0, OutputStream var1) throws IOException {
        write(var0, -1, (OutputStream)var1);
    }

    public static void write(List<BufferedImage> var0, OutputStream var1) throws IOException {
        write(var0, (int[])null, (boolean[])null, (OutputStream)var1);
    }

    public static void write(List<BufferedImage> var0, File var1) throws IOException {
        write(var0, (int[])null, (File)var1);
    }

    public static void write(List<BufferedImage> var0, int[] var1, File var2) throws IOException {
        write(var0, var1, (OutputStream)(new FileOutputStream(var2)));
    }

    public static void write(List<BufferedImage> var0, int[] var1, boolean[] var2, File var3) throws IOException {
        write(var0, var1, var2, (OutputStream)(new FileOutputStream(var3)));
    }

    public static void write(BufferedImage var0, int var1, File var2) throws IOException {
        FileOutputStream var3 = new FileOutputStream(var2);

        try {
            BufferedOutputStream var4 = new BufferedOutputStream(var3);
            write(var0, var1, (OutputStream)var4);
            var4.flush();
        } finally {
            try {
                var3.close();
            } catch (IOException var10) {
            }

        }

    }

    public static void write(BufferedImage var0, int var1, OutputStream var2) throws IOException {
        ArrayList var3 = new ArrayList(1);
        var3.add(var0);
        write(var3, new int[]{var1}, new boolean[]{false}, (OutputStream)var2);
    }

    public static void write(List<BufferedImage> var0, int[] var1, OutputStream var2) throws IOException {
        write(var0, var1, (boolean[])null, (OutputStream)var2);
    }

    public static void write(List<BufferedImage> var0, int[] var1, boolean[] var2, OutputStream var3) throws IOException {
        LittleEndianOutputStream var4 = new LittleEndianOutputStream(var3);
        int var5 = var0.size();
        writeFileHeader(var5, 1, var4);
        int var6 = 6 + var5 * 16;
        ArrayList var7 = new ArrayList(var5);
        ArrayList var8 = new ArrayList(var5);
        ArrayList var9 = null;
        if (var2 != null) {
            var9 = new ArrayList(var5);
        }

        ImageWriter var10 = null;

        int var11;
        BufferedImage var12;
        for(var11 = 0; var11 < var5; ++var11) {
            var12 = (BufferedImage)var0.get(var11);
            int var13 = var1 == null ? -1 : var1[var11];
            BufferedImage var14 = var13 == -1 ? var12 : convert(var12, var13);
            var8.add(var14);
            InfoHeader var15 = BMPEncoder.createInfoHeader(var14);
            IconEntry var16 = createIconEntry(var15);
            if (var2 != null) {
                if (var2[var11]) {
                    if (var10 == null) {
                        var10 = getPNGImageWriter();
                    }

                    byte[] var17 = encodePNG(var10, var14);
                    var9.add(var17);
                    var16.iSizeInBytes = var17.length;
                } else {
                    var9.add((Object)null);
                }
            }

            var15.iHeight *= 2;
            var16.iFileOffset = var6;
            var6 += var16.iSizeInBytes;
            var16.write(var4);
            var7.add(var15);
        }

        for(var11 = 0; var11 < var5; ++var11) {
            var12 = (BufferedImage)var0.get(var11);
            BufferedImage var18 = (BufferedImage)var8.get(var11);
            if (var2 != null && var2[var11]) {
                byte[] var20 = (byte[])var9.get(var11);
                var4.write(var20);
            } else {
                InfoHeader var19 = (InfoHeader)var7.get(var11);
                var19.write(var4);
                if (var19.sBitCount <= 8) {
                    IndexColorModel var21 = (IndexColorModel)var18.getColorModel();
                    BMPEncoder.writeColorMap(var21, var4);
                }

                writeXorBitmap(var18, var19, var4);
                writeAndBitmap(var12, var4);
            }
        }

    }

    public static void writeFileHeader(int var0, int var1, LittleEndianOutputStream var2) throws IOException {
        var2.writeShortLE((short)0);
        var2.writeShortLE((short)var1);
        var2.writeShortLE((short)var0);
    }

    public static IconEntry createIconEntry(InfoHeader var0) {
        IconEntry var1 = new IconEntry();
        var1.bWidth = var0.iWidth == 256 ? 0 : var0.iWidth;
        var1.bHeight = var0.iHeight == 256 ? 0 : var0.iHeight;
        var1.bColorCount = var0.iNumColors >= 256 ? 0 : var0.iNumColors;
        var1.bReserved = 0;
        var1.sPlanes = 1;
        var1.sBitCount = var0.sBitCount;
        int var2 = BMPEncoder.getColorMapSize(var0.sBitCount);
        int var3 = BMPEncoder.getBitmapSize(var0.iWidth, var0.iHeight, var0.sBitCount);
        int var4 = BMPEncoder.getBitmapSize(var0.iWidth, var0.iHeight, 1);
        int var5 = var0.iSize + var2 + var3 + var4;
        var1.iSizeInBytes = var5;
        var1.iFileOffset = 0;
        return var1;
    }

    public static void writeAndBitmap(BufferedImage var0, LittleEndianOutputStream var1) throws IOException {
        WritableRaster var2 = var0.getAlphaRaster();
        int var3;
        int var4;
        int var5;
        byte[] var6;
        int var9;
        int var10;
        int var11;
        int var12;
        if (var0.getColorModel() instanceof IndexColorModel && var0.getColorModel().hasAlpha()) {
            var3 = var0.getWidth();
            var4 = var0.getHeight();
            var5 = BMPEncoder.getBytesPerLine1(var3);
            var6 = new byte[var5];
            IndexColorModel var17 = (IndexColorModel)var0.getColorModel();
            WritableRaster var16 = var0.getRaster();

            for(var9 = var4 - 1; var9 >= 0; --var9) {
                for(var10 = 0; var10 < var3; ++var10) {
                    var11 = var10 / 8;
                    var12 = var10 % 8;
                    int var13 = var16.getSample(var10, var9, 0);
                    int var14 = var17.getAlpha(var13);
                    int var15 = ~var14 & 1;
                    var6[var11] = setBit(var6[var11], var12, var15);
                }

                var1.write(var6);
            }
        } else {
            int var7;
            if (var2 == null) {
                var3 = var0.getHeight();
                var4 = var0.getWidth();
                var5 = BMPEncoder.getBytesPerLine1(var4);
                var6 = new byte[var5];

                for(var7 = 0; var7 < var5; ++var7) {
                    var6[var7] = 0;
                }

                for(var7 = var3 - 1; var7 >= 0; --var7) {
                    var1.write(var6);
                }
            } else {
                var3 = var0.getWidth();
                var4 = var0.getHeight();
                var5 = BMPEncoder.getBytesPerLine1(var3);
                var6 = new byte[var5];

                for(var7 = var4 - 1; var7 >= 0; --var7) {
                    for(int var8 = 0; var8 < var3; ++var8) {
                        var9 = var8 / 8;
                        var10 = var8 % 8;
                        var11 = var2.getSample(var8, var7, 0);
                        var12 = ~var11 & 1;
                        var6[var9] = setBit(var6[var9], var10, var12);
                    }

                    var1.write(var6);
                }
            }
        }

    }

    private static byte setBit(byte var0, int var1, int var2) {
        int var3 = 1 << 7 - var1;
        var0 = (byte)(var0 & ~var3);
        var0 = (byte)(var0 | var2 << 7 - var1);
        return var0;
    }

    private static void writeXorBitmap(BufferedImage var0, InfoHeader var1, LittleEndianOutputStream var2) throws IOException {
        WritableRaster var3 = var0.getRaster();
        switch (var1.sBitCount) {
            case 1:
                BMPEncoder.write1(var3, var2);
                break;
            case 4:
                BMPEncoder.write4(var3, var2);
                break;
            case 8:
                BMPEncoder.write8(var3, var2);
                break;
            case 24:
                BMPEncoder.write24(var3, var2);
                break;
            case 32:
                WritableRaster var4 = var0.getAlphaRaster();
                BMPEncoder.write32(var3, var4, var2);
        }

    }

    public static BufferedImage convert(BufferedImage var0, int var1) {
        BufferedImage var2 = null;
        switch (var1) {
            case 1:
                var2 = ConvertUtil.convert1(var0);
                break;
            case 4:
                var2 = ConvertUtil.convert4(var0);
                break;
            case 8:
                var2 = ConvertUtil.convert8(var0);
                break;
            case 24:
                int var3 = var0.getColorModel().getPixelSize();
                if (var3 != 24 && var3 != 32) {
                    var2 = ConvertUtil.convert24(var0);
                } else {
                    var2 = var0;
                }
                break;
            case 32:
                int var4 = var0.getColorModel().getPixelSize();
                if (var4 != 24 && var4 != 32) {
                    var2 = ConvertUtil.convert32(var0);
                } else {
                    var2 = var0;
                }
        }

        return var2;
    }

    private static ImageWriter getPNGImageWriter() {
        ImageWriter var0 = null;
        Iterator var1 = ImageIO.getImageWritersByFormatName("png");
        if (var1.hasNext()) {
            var0 = (ImageWriter)var1.next();
        }

        return var0;
    }

    private static byte[] encodePNG(ImageWriter var0, BufferedImage var1) throws IOException {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        ImageOutputStream var3 = ImageIO.createImageOutputStream(var2);
        var0.setOutput(var3);
        var0.write(var1);
        var2.flush();
        byte[] var4 = var2.toByteArray();
        return var4;
    }
}
