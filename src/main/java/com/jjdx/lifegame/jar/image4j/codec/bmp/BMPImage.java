//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.codec.bmp;

import java.awt.image.BufferedImage;

public class BMPImage {
    protected InfoHeader infoHeader;
    protected BufferedImage image;

    public BMPImage(BufferedImage var1, InfoHeader var2) {
        this.image = var1;
        this.infoHeader = var2;
    }

    public InfoHeader getInfoHeader() {
        return this.infoHeader;
    }

    public void setInfoHeader(InfoHeader var1) {
        this.infoHeader = var1;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage var1) {
        this.image = var1;
    }

    public int getWidth() {
        return this.infoHeader == null ? -1 : this.infoHeader.iWidth;
    }

    public int getHeight() {
        return this.infoHeader == null ? -1 : this.infoHeader.iHeight;
    }

    public int getColourDepth() {
        return this.infoHeader == null ? -1 : this.infoHeader.sBitCount;
    }

    public int getColourCount() {
        short var1 = this.infoHeader.sBitCount == 32 ? 24 : this.infoHeader.sBitCount;
        return var1 == -1 ? -1 : 1 << var1;
    }

    public boolean isIndexed() {
        return this.infoHeader == null ? false : this.infoHeader.sBitCount <= 8;
    }
}
