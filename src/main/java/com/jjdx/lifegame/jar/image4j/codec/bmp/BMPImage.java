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


    public BufferedImage getImage() {
        return this.image;
    }


}
