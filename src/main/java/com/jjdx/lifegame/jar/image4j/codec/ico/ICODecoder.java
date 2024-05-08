//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.codec.ico;

import com.jjdx.lifegame.jar.image4j.codec.bmp.BMPDecoder;
import com.jjdx.lifegame.jar.image4j.codec.bmp.ColorEntry;
import com.jjdx.lifegame.jar.image4j.codec.bmp.InfoHeader;
import com.jjdx.lifegame.jar.image4j.io.CountingInputStream;
import com.jjdx.lifegame.jar.image4j.io.EndianUtils;
import com.jjdx.lifegame.jar.image4j.io.LittleEndianInputStream;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
public class ICODecoder {
    private static Logger log = Logger.getLogger(ICODecoder.class.getName());
    private static final int PNG_MAGIC = -1991225785;
    private static final int PNG_MAGIC_LE = 1196314761;
    private static final int PNG_MAGIC2 = 218765834;
    private static final int PNG_MAGIC2_LE = 169478669;

    private ICODecoder() {
    }

    public static List<BufferedImage> read(File var0) throws IOException {
        FileInputStream var1 = new FileInputStream(var0);

        List var2;
        try {
            var2 = read((InputStream)(new BufferedInputStream(var1)));
        } finally {
            try {
                var1.close();
            } catch (IOException var9) {
                log.log(Level.FINE, "Failed to close file input for file " + var0);
            }

        }

        return var2;
    }

    public static List<ICOImage> readExt(File var0) throws IOException {
        FileInputStream var1 = new FileInputStream(var0);

        List var2;
        try {
            var2 = readExt((InputStream)(new BufferedInputStream(var1)));
        } finally {
            try {
                var1.close();
            } catch (IOException var9) {
                log.log(Level.WARNING, "Failed to close file input for file " + var0, var9);
            }

        }

        return var2;
    }

    public static List<BufferedImage> read(InputStream var0) throws IOException {
        List var1 = readExt(var0);
        ArrayList var2 = new ArrayList(var1.size());

        for(int var3 = 0; var3 < var1.size(); ++var3) {
            ICOImage var4 = (ICOImage)var1.get(var3);
            BufferedImage var5 = var4.getImage();
            var2.add(var5);
        }

        return var2;
    }

    private static IconEntry[] sortByFileOffset(IconEntry[] var0) {
        List var1 = Arrays.asList(var0);
        Collections.sort(var1, new Comparator<IconEntry>() {
            public int compare(IconEntry var1, IconEntry var2) {
                return var1.iFileOffset - var2.iFileOffset;
            }
        });
        return (IconEntry[])var1.toArray(new IconEntry[var1.size()]);
    }

    public static List<ICOImage> readExt(InputStream var0) throws IOException {
        LittleEndianInputStream var1 = new LittleEndianInputStream(new CountingInputStream(var0));
        short var2 = var1.readShortLE();
        short var3 = var1.readShortLE();
        short var4 = var1.readShortLE();
        IconEntry[] var5 = new IconEntry[var4];

        int var6;
        for(var6 = 0; var6 < var4; var6 = (short)(var6 + 1)) {
            var5[var6] = new IconEntry(var1);
        }

        byte var32 = 0;
        ArrayList var7 = new ArrayList(var4);

        try {
            for(var6 = 0; var6 < var4; ++var6) {
                int var8 = var1.getCount();
                if (var8 != var5[var6].iFileOffset) {
                    throw new IOException("Cannot read image #" + var6 + " starting at unexpected file offset.");
                }

                int var9 = var1.readIntLE();
                log.log(Level.FINE, "Image #" + var6 + " @ " + var1.getCount() + " info = " + EndianUtils.toInfoString(var9));
                if (var9 != 40) {
                    if (var9 != 1196314761) {
                        throw new IOException("Unrecognized icon format for image #" + var6);
                    }

                    int var33 = var1.readIntLE();
                    if (var33 != 169478669) {
                        throw new IOException("Unrecognized icon format for image #" + var6);
                    }

                    IconEntry var34 = var5[var6];
                    int var35 = var34.iSizeInBytes - 8;
                    byte[] var36 = new byte[var35];
                    var1.readFully(var36);
                    ByteArrayOutputStream var37 = new ByteArrayOutputStream();
                    DataOutputStream var38 = new DataOutputStream(var37);
                    var38.writeInt(-1991225785);
                    var38.writeInt(218765834);
                    var38.write(var36);
                    byte[] var41 = var37.toByteArray();
                    ByteArrayInputStream var44 = new ByteArrayInputStream(var41);
                    ImageInputStream var46 = ImageIO.createImageInputStream(var44);
                    ImageReader var48 = getPNGImageReader();
                    var48.setInput(var46);
                    BufferedImage var50 = var48.read(0);
                    IconEntry var52 = var5[var6];
                    ICOImage var54 = new ICOImage(var50, (InfoHeader)null, var52);
                    var54.setPngCompressed(true);
                    var54.setIconIndex(var6);
                    var7.add(var54);
                } else {
                    InfoHeader var10 = BMPDecoder.readInfoHeader(var1, var9);
                    InfoHeader var11 = new InfoHeader(var10);
                    var11.iHeight = var10.iHeight / 2;
                    InfoHeader var12 = new InfoHeader(var10);
                    var12.iHeight = var11.iHeight;
                    var11.sBitCount = 1;
                    var11.iNumColors = 2;
                    BufferedImage var13 = BMPDecoder.read(var12, var1);
                    BufferedImage var14 = new BufferedImage(var12.iWidth, var12.iHeight, 2);
                    ColorEntry[] var15 = new ColorEntry[]{new ColorEntry(255, 255, 255, 255), new ColorEntry(0, 0, 0, 0)};
                    int var25;
                    int var26;
                    int var27;
                    if (var10.sBitCount == 32) {
                        int var16 = var5[var6].iSizeInBytes;
                        int var17 = var10.iSize;
                        int var18 = var12.iWidth * var12.iHeight * 4;
                        int var19 = var16 - var17 - var18;
                        int var20 = var5[var6].iFileOffset + var16 - var1.getCount();
                        if (var1.skip(var19, false) < var19 && var6 < var4 - 1) {
                            throw new EOFException("Unexpected end of input");
                        }

                        WritableRaster var21 = var13.getRaster();
                        WritableRaster var22 = var13.getAlphaRaster();
                        WritableRaster var23 = var14.getRaster();
                        WritableRaster var24 = var14.getAlphaRaster();

                        for(var25 = var12.iHeight - 1; var25 >= 0; --var25) {
                            for(var26 = 0; var26 < var12.iWidth; ++var26) {
                                var27 = var21.getSample(var26, var25, 0);
                                int var28 = var21.getSample(var26, var25, 1);
                                int var29 = var21.getSample(var26, var25, 2);
                                int var30 = var22.getSample(var26, var25, 0);
                                var23.setSample(var26, var25, 0, var27);
                                var23.setSample(var26, var25, 1, var28);
                                var23.setSample(var26, var25, 2, var29);
                                var24.setSample(var26, var25, 0, var30);
                            }
                        }
                    } else {
                        BufferedImage var39 = BMPDecoder.read(var11, var1, var15);
                        WritableRaster var42 = var13.getRaster();
                        WritableRaster var45 = var14.getRaster();
                        WritableRaster var47 = var14.getAlphaRaster();
                        WritableRaster var49 = var39.getRaster();

                        for(int var51 = 0; var51 < var12.iHeight; ++var51) {
                            for(int var53 = 0; var53 < var12.iWidth; ++var53) {
                                var26 = var13.getRGB(var53, var51);
                                int var55 = var26 >> 16 & 255;
                                int var56 = var26 >> 8 & 255;
                                var25 = var26 & 255;
                                var45.setSample(var53, var51, 0, var55);
                                var45.setSample(var53, var51, 1, var56);
                                var45.setSample(var53, var51, 2, var25);
                                var27 = var39.getRGB(var53, var51);
                                var47.setSample(var53, var51, 0, var27);
                            }
                        }
                    }

                    IconEntry var40 = var5[var6];
                    ICOImage var43 = new ICOImage(var14, var10, var40);
                    var43.setPngCompressed(false);
                    var43.setIconIndex(var6);
                    var7.add(var43);
                }
            }

            return var7;
        } catch (IOException var31) {
            throw new IOException("Failed to read image # " + var32, var31);
        }
    }

    private static ImageReader getPNGImageReader() {
        ImageReader var0 = null;
        Iterator var1 = ImageIO.getImageReadersByFormatName("png");
        if (var1.hasNext()) {
            var0 = (ImageReader)var1.next();
        }

        return var0;
    }
}
