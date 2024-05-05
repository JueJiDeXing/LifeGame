package com.jjdx.lifegame.Plugins;

/**
 一些帮助方法
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class Util {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int[][] eightDir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};


    public static boolean isValid(int x, int y, int n, int m) {
        return 0 <= x && x < n && 0 <= y && y < m;
    }

}
