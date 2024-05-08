package com.jjdx.lifegame.Plugins;

import com.jjdx.lifegame.jar.SwingFX.SwingFXUtils;
import com.jjdx.lifegame.jar.image4j.codec.ico.ICODecoder;

import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 图标管理
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/7 <br> */
public class ICONer {
    static HashMap<String, WritableImage> cache = new HashMap<>();

    /**
     获取图标对象

     @param fileName 图标文件名
     */
    public static WritableImage getIcon(String fileName) {
        if (cache.containsKey(fileName)) return cache.get(fileName);
        String filePath = FileUtil.findFilePath(fileName);
        if (filePath == null) {
            MyLogger.warn("ICONer - 找不到图标文件: " + fileName);
            throw new RuntimeException(" 找不到图标文件: " + fileName);
        }
        MyLogger.info("ICONer - findPath");
        try {
            BufferedImage icon = ICODecoder.read(new File(filePath)).get(0);//ICODecoder无法导入
            MyLogger.info("ICONer - convert");
            WritableImage ans = SwingFXUtils.toFXImage(icon, null);//SwingFXUtils也无法导入
            cache.put(fileName, ans);
            MyLogger.info("ICONer - success");
            return ans;
        } catch (IOException e) {
            MyLogger.warn("ICONer - 转换图标失败");
            throw new RuntimeException("转换图标失败");
        }
    }
}
