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


    public void setPngCompressed(boolean var1) {
        this.pngCompressed = var1;
    }

    public InfoHeader getInfoHeader() {
        return super.getInfoHeader();
    }


    public void setIconIndex(int var1) {
        this.iconIndex = var1;
    }

}
