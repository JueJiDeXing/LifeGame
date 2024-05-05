package com.jjdx.lifegame.Plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 加载器,可以用于查找文件路径、加载类信息等
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/2 <br> */
public class Loader {
    private static final String workDir = System.getProperty("user.dir");
    private static final HashMap<String, String> cache = new HashMap<>();

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
            if (file.isDirectory()) {
                //是文件夹
                if (isToFindFile) {//是找file的,深搜
                    String path = dfs(file.getAbsolutePath(), fileName, isToFindFile);
                    if (path != null) return path;
                } else {//是找文件夹的
                    if (file.getName().equals(fileName)) return file.getAbsolutePath();
                }
            } else {
                //是文件
                if (isToFindFile && file.getName().equals(fileName)) {//是file,且找file
                    return file.getAbsolutePath();
                }
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

    public static Class<?>[] getAllClasses() {
        List<Class<?>> classList = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String packageName = "com.jjdx.lifegame";
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                if (!directory.exists()) continue;
                add(packageName, directory, classList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList.toArray(new Class<?>[0]);
    }

    private static void add(String packageName, File directory, List<Class<?>> classList) throws ClassNotFoundException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                add(packageName + "." + file.getName(), file, classList);
            } else {
                String name = file.getName();
                if (name.endsWith(".class")) {
                    String className = packageName + '.' + name.substring(0, name.length() - 6);
                    Class<?> clazz = Class.forName(className);
                    classList.add(clazz);
                }
            }
        }
    }
}
