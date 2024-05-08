//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.codec.ico;

import com.jjdx.lifegame.jar.image4j.codec.bmp.BMPImage;
import com.jjdx.lifegame.jar.image4j.codec.bmp.InfoHeader;

import java.awt.image.BufferedImage;

public class ICOImage extends BMPImage {
    protected IconEntry iconEntry;
    protected boolean pngCompressed = false;
    protected int iconIndex = -1;

    public ICOImage(BufferedImage var1, InfoHeader var2, IconEntry var3) {
        super(var1, var2);
        this.iconEntry = var3;
    }

    public IconEntry getIconEntry() {
        return this.iconEntry;
    }

    public void setIconEntry(IconEntry var1) {
        this.iconEntry = var1;
    }

    public boolean isPngCompressed() {
        return this.pngCompressed;
    }

    public void setPngCompressed(boolean var1) {
        this.pngCompressed = var1;
    }

    public InfoHeader getInfoHeader() {
        return super.getInfoHeader();
    }

    public int getIconIndex() {
        return this.iconIndex;
    }

    public void setIconIndex(int var1) {
        this.iconIndex = var1;
    }

    public int getWidth() {
        return this.iconEntry == null ? -1 : (this.iconEntry.bWidth == 0 ? 256 : this.iconEntry.bWidth);
    }

    public int getHeight() {
        return this.iconEntry == null ? -1 : (this.iconEntry.bHeight == 0 ? 256 : this.iconEntry.bHeight);
    }

    public int getColourDepth() {
        return this.iconEntry == null ? -1 : this.iconEntry.sBitCount;
    }

    public int getColourCount() {
        short var1 = this.iconEntry.sBitCount == 32 ? 24 : this.iconEntry.sBitCount;
        return var1 == -1 ? -1 : 1 << var1;
    }

    public boolean isIndexed() {
        return this.iconEntry == null ? false : this.iconEntry.sBitCount <= 8;
    }
}
