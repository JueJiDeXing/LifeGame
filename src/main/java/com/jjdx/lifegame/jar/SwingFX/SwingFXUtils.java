//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.SwingFX;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.nio.IntBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

public class SwingFXUtils {
    private SwingFXUtils() {
    }

    public static WritableImage toFXImage(BufferedImage var0, WritableImage var1) {
        int var2 = var0.getWidth();
        int var3 = var0.getHeight();
        switch (var0.getType()) {
            case 2:
            case 3:
                break;
            default:
                BufferedImage var4 = new BufferedImage(var2, var3, 3);
                Graphics2D var5 = var4.createGraphics();
                var5.drawImage(var0, 0, 0, (ImageObserver)null);
                var5.dispose();
                var0 = var4;
        }

        int[] var6;
        if (var1 != null) {
            int var11 = (int)var1.getWidth();
            int var13 = (int)var1.getHeight();
            if (var11 >= var2 && var13 >= var3) {
                if (var2 < var11 || var3 < var13) {
                    var6 = new int[var11];
                    PixelWriter var7 = var1.getPixelWriter();
                    WritablePixelFormat var8 = PixelFormat.getIntArgbPreInstance();
                    if (var2 < var11) {
                        var7.setPixels(var2, 0, var11 - var2, var3, var8, var6, 0, 0);
                    }

                    if (var3 < var13) {
                        var7.setPixels(0, var3, var11, var13 - var3, var8, var6, 0, 0);
                    }
                }
            } else {
                var1 = null;
            }
        }

        if (var1 == null) {
            var1 = new WritableImage(var2, var3);
        }

        PixelWriter var12 = var1.getPixelWriter();
        DataBufferInt var14 = (DataBufferInt)var0.getRaster().getDataBuffer();
        var6 = var14.getData();
        int var15 = var0.getRaster().getDataBuffer().getOffset();
        int var16 = 0;
        SampleModel var9 = var0.getRaster().getSampleModel();
        if (var9 instanceof SinglePixelPackedSampleModel) {
            var16 = ((SinglePixelPackedSampleModel)var9).getScanlineStride();
        }

        WritablePixelFormat var10 = var0.isAlphaPremultiplied() ? PixelFormat.getIntArgbPreInstance() : PixelFormat.getIntArgbInstance();
        var12.setPixels(0, 0, var2, var3, var10, var6, var15, var16);
        return var1;
    }

    static int getBestBufferedImageType(PixelFormat<?> var0, BufferedImage var1, boolean var2) {
        if (var1 != null) {
            int var3 = var1.getType();
            if (var3 == 2 || var3 == 3 || var2 && (var3 == 4 || var3 == 1)) {
                return var3;
            }
        }

        switch (var0.getType()) {
            case BYTE_BGRA_PRE:
            case INT_ARGB_PRE:
            default:
                return 3;
            case BYTE_BGRA:
            case INT_ARGB:
                return 2;
            case BYTE_RGB:
                return 1;
            case BYTE_INDEXED:
                return var0.isPremultiplied() ? 3 : 2;
        }
    }

    private static WritablePixelFormat<IntBuffer> getAssociatedPixelFormat(BufferedImage var0) {
        switch (var0.getType()) {
            case 1:
            case 3:
                return PixelFormat.getIntArgbPreInstance();
            case 2:
                return PixelFormat.getIntArgbInstance();
            default:
                throw new InternalError("Failed to validate BufferedImage type");
        }
    }

    private static boolean checkFXImageOpaque(PixelReader var0, int var1, int var2) {
        for(int var3 = 0; var3 < var1; ++var3) {
            for(int var4 = 0; var4 < var2; ++var4) {
                Color var5 = var0.getColor(var3, var4);
                if (var5.getOpacity() != 1.0) {
                    return false;
                }
            }
        }

        return true;
    }

    public static BufferedImage fromFXImage(Image var0, BufferedImage var1) {
        PixelReader var2 = var0.getPixelReader();
        if (var2 == null) {
            return null;
        } else {
            int var3 = (int)var0.getWidth();
            int var4 = (int)var0.getHeight();
            PixelFormat var5 = var2.getPixelFormat();
            boolean var6 = false;
            switch (var5.getType()) {
                case BYTE_BGRA_PRE:
                case INT_ARGB_PRE:
                case BYTE_BGRA:
                case INT_ARGB:
                    if (var1 != null && (var1.getType() == 4 || var1.getType() == 1)) {
                        var6 = checkFXImageOpaque(var2, var3, var4);
                    }
                    break;
                case BYTE_RGB:
                    var6 = true;
            }

            int var7 = getBestBufferedImageType(var2.getPixelFormat(), var1, var6);
            if (var1 != null) {
                int var8 = var1.getWidth();
                int var9 = var1.getHeight();
                if (var8 >= var3 && var9 >= var4 && var1.getType() == var7) {
                    if (var3 < var8 || var4 < var9) {
                        Graphics2D var10 = var1.createGraphics();
                        var10.setComposite(AlphaComposite.Clear);
                        var10.fillRect(0, 0, var8, var9);
                        var10.dispose();
                    }
                } else {
                    var1 = null;
                }
            }

            if (var1 == null) {
                var1 = new BufferedImage(var3, var4, var7);
            }

            DataBufferInt var14 = (DataBufferInt)var1.getRaster().getDataBuffer();
            int[] var15 = var14.getData();
            int var16 = var1.getRaster().getDataBuffer().getOffset();
            int var11 = 0;
            SampleModel var12 = var1.getRaster().getSampleModel();
            if (var12 instanceof SinglePixelPackedSampleModel) {
                var11 = ((SinglePixelPackedSampleModel)var12).getScanlineStride();
            }

            WritablePixelFormat var13 = getAssociatedPixelFormat(var1);
            var2.getPixels(0, 0, var3, var4, var13, var15, var16, var11);
            return var1;
        }
    }
}
