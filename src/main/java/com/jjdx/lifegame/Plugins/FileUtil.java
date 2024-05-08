package com.jjdx.lifegame.Plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 文件助理,可以用于查找文件路径、加载类信息等
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/2 <br> */
public class FileUtil {
    private static final String workDir = System.getProperty("user.dir");
    private static final HashMap<String, String> cache = new HashMap<>();

    private FileUtil() {
    }

    /**
     按文件名在项目中查找文件路径

     @param fileName 要查找的文件名(带后缀)
     @return 该文件的路径信息(不存在为null)
     */
    public static String findFilePath(String fileName) {
        if (cache.containsKey(fileName)) return cache.get(fileName);
        //从workDir进行深度搜索,匹配文件名
        String res = dfs(workDir, fileName, true);
        cache.put(fileName, res);
        return res;
    }

    private static String dfs(String cur, String fileName, boolean isToFindFile) {
        File curFile = new File(cur);
        if (curFile.getName().equals(fileName)) return curFile.getAbsolutePath();
        if (!curFile.isDirectory()) return null;//单文件,前面判断了文件名是否相同
        //是文件夹
        File[] files = curFile.listFiles();
        if (files == null) return null;//空的
        //进入搜索
        for (File file : files) {
            if (!file.isDirectory()) {
                //是文件
                if (isToFindFile && file.getName().equals(fileName)) return file.getAbsolutePath();//是file,且找file
                continue;
            }
            //是文件夹
            String path;
            if (isToFindFile && (path = dfs(file.getAbsolutePath(), fileName, true)) != null) {//是找file的,深搜
                return path;
            } else if (file.getName().equals(fileName)) {
                return file.getAbsolutePath(); //是找文件夹的
            }
        }
        return null;
    }

    /**
     读取文件

     @param file 文件路径
     */
    public static String readFile(String file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("\n")) break;
                sb.append(line).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);//最后一个换行符不要
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     写入信息

     @param file 文件路径
     */
    public static void writeFile(String file, String info) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(info);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     按文件名在项目中查找文件夹

     @param dirName 要查找的文件夹名
     @return 该文件夹的路径信息(不存在为null)
     */
    public static String findDirPath(String dirName) {
        if (cache.containsKey(dirName)) return cache.get(dirName);
        //从workDir进行深度搜索,匹配文件名
        String res = dfs(workDir, dirName, false);
        cache.put(dirName, res);
        return res;
    }

    /**
     获取package下的所有文件

     @param packagePath package路径
     @return 文件路径
     */
    public static List<String> getAll(String packagePath) {
        List<String> ans = new ArrayList<>();
        getAll(ans, new File(packagePath));
        return ans;

    }

    private static void getAll(List<String> ans, File file) {
        if (!file.isDirectory()) {
            ans.add(file.getAbsolutePath());
            return;
        }
        File[] files = file.listFiles();
        if (files == null) return;//空的
        for (File f : files) {
            if (!f.isDirectory()) {
                ans.add(f.getAbsolutePath());
                continue;
            }
            getAll(ans, f);
        }
    }

}
