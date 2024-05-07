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
 Yaml配置文件的读取器, (需要在程序启动前加载信息)
 <br>
 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/7 <br>
 */
public class Config {

    private static HashMap<String, Object> yMap;//从配置文件读取的信息
    private static final HashMap<String, Object> cache = new HashMap<>();

    private Config() {
    }

    static {
        try {
            yMap = new Yaml().load(Config.class.getClassLoader().getResourceAsStream("config.yml"));
        } catch (Exception e) {
            throw new RuntimeException(" error:未成功读取配置文件 ");
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

    public static <T> T get(String name, T defaultValue) {
        if (cache.containsKey(name)) return (T) cache.get(name);
        Object value = split(name, defaultValue);
        if (!Util.isInstanceOf(value, defaultValue.getClass())) {
            cache.put(name, defaultValue);
            return defaultValue;
        }
        T ans = (T) value;
        cache.put(name, ans);
        return ans;

    }

    /**
     获取配置文件的值

     @param name 以"."分隔的键名
     @return 值, 若不存在则返回默认值0
     */
    public static int getInt(String name) {
        if (cache.containsKey(name)) return (int) cache.get(name);
        return getInt(name, 0);
    }

    public static int getInt(String name, int defaultVal) {
        if (cache.containsKey(name)) return (int) cache.get(name);
        Object value = split(name, defaultVal);
        if (!Util.isInstanceOf(value, Integer.class)) {
            cache.put(name, defaultVal);
            return defaultVal;
        }
        int ans = (int) value;
        cache.put(name, ans);
        return ans;

    }

    public static String getString(String name) {
        if (cache.containsKey(name)) return (String) cache.get(name);
        return getString(name, "");
    }

    public static String getString(String name, String defaultVal) {
        if (cache.containsKey(name)) return (String) cache.get(name);
        Object value = split(name, defaultVal);
        if (!Util.isInstanceOf(value, String.class)) {
            cache.put(name, defaultVal);
            return defaultVal;
        }
        String ans = (String) value;
        cache.put(name, ans);
        return ans;

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

    public static boolean getBoolean(String name) {
        Object value = split(name, false);
        if (!Util.isInstanceOf(value, Boolean.class)) {
            return false;
        }
        return (boolean) value;
    }
}
