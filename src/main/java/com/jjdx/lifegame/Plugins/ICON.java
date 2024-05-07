package com.jjdx.lifegame.Plugins;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import net.sf.image4j.codec.ico.ICODecoder;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 图标管理
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/7 <br> */
public class ICON {
    static HashMap<String, WritableImage> cache = new HashMap<>();

    public static WritableImage getIcon(String fileName) throws IOException {
        if (cache.containsKey(fileName)) return cache.get(fileName);
        String filePath = Loader.findFilePath(Config.getString(fileName));
        if (filePath == null) {
            throw new RuntimeException("找不到图标文件, " +
                    "fileName=" + fileName +
                    ", config.load=" + Config.getString(fileName) +
                    ", Loader.path=" + null);
        }
        WritableImage ans = SwingFXUtils.toFXImage(ICODecoder.read(new File(filePath)).get(0), null);
        cache.put(fileName, ans);
        return ans;
    }
}
