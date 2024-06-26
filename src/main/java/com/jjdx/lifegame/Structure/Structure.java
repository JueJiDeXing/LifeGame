package com.jjdx.lifegame.Structure;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 一类图形
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/7 <br> */
public class Structure {

    String name;//类名
    List<Pair<String, List<int[]>>> struct = new ArrayList<>();//每个图形
    Integer rowMaxLen = null, colMaxLen = null;

    public Structure(String name) {
        this.name = name;
    }

    public void clear() {
        struct.clear();
        rowMaxLen = null;
        colMaxLen = null;
    }

    public int[] getMaxLen() {
        if (rowMaxLen == null || colMaxLen == null) {
            // 没有值则计算后保存副本
            int[] len = calMaxLen(struct);
            rowMaxLen = len[0];
            colMaxLen = len[1];
        }
        return new int[]{rowMaxLen, colMaxLen};
    }

    /**
     计算图像的行列最大值
     */
    public int[] calMaxLen(List<Pair<String, List<int[]>>> structs) {
        int maxRow = 0, maxCol = 0;
        for (var struct : structs) {
            List<int[]> poss = struct.getValue();//每一个结构的位置信息
            for (var p : poss) {
                maxRow = Math.max(maxRow, p[0]);
                maxCol = Math.max(maxCol, p[1]);
            }
        }
        return new int[]{maxRow + 1, maxCol + 1};
    }

    public int getSize() {
        return struct.size();
    }

    public boolean isEmpty() {
        return struct.isEmpty();
    }

    public Pair<String, List<int[]>> getFirst() {
        return struct.get(0);
    }

    public Pair<String, List<int[]>> get(int index) {
        return struct.get(index);
    }

    public String getName() {
        return name;
    }

}
