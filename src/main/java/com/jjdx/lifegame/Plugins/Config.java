package com.jjdx.lifegame.Plugins;

import java.io.*;
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

    private static HashMap<String, Object> yMap;//从配置文件读取的信息

    private Config() {
    }

    static {
        try {
            yMap = new Yaml().load(Config.class.getClassLoader().getResourceAsStream("config.yml"));
        } catch (Exception e) {
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

    /**
     获取配置文件的值
     * @param name 以"."分隔的键名
     * @return 值,若不存在则返回默认值0
     */
    public static int getInt(String name) {
        return getInt(name, 0);
    }

    public static int getInt(String name, int defaultVal) {
        Object value = split(name, defaultVal);
        if (value == null) return defaultVal;
        if (value instanceof Integer) return (int) value;
        return defaultVal;
    }

    public static String getString(String name) {
        return getString(name, "");
    }

    public static String getString(String name, String defaultVal) {
        Object value = split(name, defaultVal);
        if (value == null) return defaultVal;
        if (value instanceof String) return (String) value;
        return defaultVal;
    }

    /**
     分割name, 或取最后一个值
     */
    public static Object split(String name, Object defaultVal) {
        String[] split = name.split("\\.");
        HashMap<String, Object> m = new HashMap<>(yMap);
        int n = split.length;
        for (int i = 0; i < n - 1; i++) {
            if (!m.containsKey(split[i])) return defaultVal;
            Object o = m.get(split[i]);
            if (o instanceof HashMap) {
                m = (HashMap<String, Object>) o;
            } else {
                return defaultVal;
            }
        }
        return m.get(split[n - 1]);
    }
}
