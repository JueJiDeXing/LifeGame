package com.jjdx.lifegame.Plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;

import javafx.scene.effect.Reflection;
import org.yaml.snakeyaml.Yaml;

/**
 Yaml配置文件的读取器
 */
public class Config {

    private static HashMap<String, Object> yMap;

    static {
        try {
            yMap = new Yaml().load(new FileInputStream("config.yml"));//默认
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setYML(String fileName) {
        if (fileName == null) return;
        if (!fileName.endsWith(".yml")) return;
        try {
            yMap = new Yaml().load(new FileInputStream(fileName));//默认
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getInt(String name) {
        String[] split = name.split("\\.");
        HashMap<String, Object> m = new HashMap<>(yMap);
        int n = split.length;
        for (int i = 0; i < n - 1; i++) {
            if (!m.containsKey(split[i])) return 0;
            Object o = m.get(split[i]);
            if (o instanceof HashMap) {
                m = (HashMap<String, Object>) o;
            } else {
                return 0;
            }
        }
        Object value = m.get(split[n - 1]);
        if (value == null) return 0;
        if (value instanceof Integer) return (int) value;
        return 0;
    }
}
