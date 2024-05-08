//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ImageUtil {
    public ImageUtil() {
    }

    public static BufferedImage scaleImage(BufferedImage var0, int var1, int var2) {
        Image var3 = var0.getScaledInstance(var1, var2, 0);
        BufferedImage var4 = null;
        var4 = new BufferedImage(var1, var2, 2);
        Graphics2D var5 = var4.createGraphics();
        var5.drawImage(var3, 0, 0, (ImageObserver)null);
        return var4;
    }
}
