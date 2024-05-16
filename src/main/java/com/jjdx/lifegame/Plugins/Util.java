package com.jjdx.lifegame.Plugins;

/**
 一些帮助方法
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class Util {
    private Util() {
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T, U> boolean isInstanceOf(T obj, Class<U> clazz) {
        return clazz.isInstance(obj);//null->false
    }

    public static int[][] eightDir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    static int[][] fourDir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public static boolean isValid(int x, int y, int n, int m) {
        return 0 <= x && x < n && 0 <= y && y < m;
    }

    public static boolean isValid(int[] pos, int n, int m) {
        return 0 <= pos[0] && pos[0] < n && 0 <= pos[1] && pos[1] < m;
    }

    public static int max(int x, int... y) {
        for (int t : y) x = Math.max(x, t);
        return x;
    }
}
