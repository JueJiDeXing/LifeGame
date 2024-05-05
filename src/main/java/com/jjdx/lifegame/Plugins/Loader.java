package com.jjdx.lifegame.Plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 加载器,可以用于查找文件路径
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/2 <br> */
public class Loader {
    private static final String workDir = System.getProperty("user.dir");
    private static final HashMap<String, String> cache = new HashMap<>();

    public static String findFilePath(String fileName) {
        if (cache.containsKey(fileName)) return cache.get(fileName);
        //从workDir进行深度搜索,匹配文件名
        String res = dfs(workDir, fileName);
        cache.put(fileName, res);
        return res;
    }

    private static String dfs(String dir, String fileName) {
        File dirFile = new File(dir);
        if (!dirFile.isDirectory()) return null;
        File[] files = dirFile.listFiles();
        if (files == null) return null;
        for (File file : files) {
            if (file.isDirectory()) {
                String path = dfs(file.getAbsolutePath(), fileName);
                if (path != null) {
                    return path;
                }
            }
            if (file.getName().equals(fileName)) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    public static String readFile(String file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (Exception ignored) {
            return "读取失败,请联系管理员";
        }
        return sb.toString();
    }

}
