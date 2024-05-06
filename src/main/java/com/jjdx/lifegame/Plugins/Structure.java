package com.jjdx.lifegame.Plugins;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 结构: [静物|震荡器|滑翔机|繁殖|长寿]
 <br>
 TODO 此处代码有些冗余,需要重构
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class Structure {
    private Structure(){}
    //名称,位置信息
    public static List<Pair<String, List<Pair<Integer, Integer>>>>
            still = new ArrayList<>(), oscillator = new ArrayList<>(),
            fly = new ArrayList<>(), reproduction = new ArrayList<>(),
            longLife = new ArrayList<>();

    public static Integer stillRowMaxLen = null, stillColMaxLen = null,
            oscillatorRowMaxLen = null, oscillatorColMaxLen = null,
            flyRowMaxLen = null, flyColMaxLen = null,
            reproductionRowMaxLen = null, reproductionColMaxLen = null,
            longLifeRowMaxLen = null, longLifeColMaxLen = null;

    static List<String> types = new ArrayList<>(Arrays.asList("still", "oscillator", "fly", "reproduction", "longLife"));
    static HashMap<String, String> typeNameToPath = new HashMap<>();

    static {
        for (String type : types) {
            typeNameToPath.put(type, Loader.findFilePath(type + ".txt"));
        }
        reload();
    }

    /**
     重新加载图形
     */
    public static void reload() {
        still.clear();
        oscillator.clear();
        fly.clear();
        reproduction.clear();
        longLife.clear();
        load(still, "still");
        load(oscillator, "oscillator");
        load(fly, "fly");
        load(reproduction, "reproduction");
        load(longLife, "longLife");
    }

    /**
     加载type类型的图形到structs
     */
    static void load(List<Pair<String, List<Pair<Integer, Integer>>>> structs, String type) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(typeNameToPath.get(type)));
            br.lines().forEach(line -> {
                String[] split = line.split(" ");
                String name = split[0];
                int[] poss = Arrays.stream(split).skip(1).mapToInt(Integer::parseInt).toArray();
                List<Pair<Integer, Integer>> map = new ArrayList<>();
                for (int i = 0; i < poss.length; i += 2) {
                    map.add(new Pair<>(poss[i], poss[i + 1]));
                }
                structs.add(new Pair<>(name, map));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int[] getStillMaxLen() {
        if (stillRowMaxLen == null || stillColMaxLen == null) {
            int[] len = calMaxLen(still);
            stillRowMaxLen = len[0];
            stillColMaxLen = len[1];
        }
        return new int[]{stillRowMaxLen, stillColMaxLen};
    }

    public static int[] getOscillatorMaxLen() {
        if (oscillatorRowMaxLen == null || oscillatorColMaxLen == null) {
            int[] len = calMaxLen(oscillator);
            oscillatorRowMaxLen = len[0];
            oscillatorColMaxLen = len[1];
        }
        return new int[]{oscillatorRowMaxLen, oscillatorColMaxLen};
    }

    public static int[] getFlyMaxLen() {
        if (flyRowMaxLen == null || flyColMaxLen == null) {
            int[] len = calMaxLen(fly);
            flyRowMaxLen = len[0];
            flyColMaxLen = len[1];
        }
        return new int[]{flyRowMaxLen, flyColMaxLen};
    }

    public static int[] getReproductionMaxLen() {
        if (reproductionRowMaxLen == null || reproductionColMaxLen == null) {
            int[] len = calMaxLen(reproduction);
            reproductionRowMaxLen = len[0];
            reproductionColMaxLen = len[1];
        }
        return new int[]{reproductionRowMaxLen, reproductionColMaxLen};
    }

    public static int[] getLongLifeRowMaxLenMaxLen() {
        if (longLifeRowMaxLen == null || longLifeColMaxLen == null) {
            int[] len = calMaxLen(longLife);
            longLifeRowMaxLen = len[0];
            longLifeColMaxLen = len[1];
        }
        return new int[]{longLifeRowMaxLen, longLifeColMaxLen};
    }


    public static int[] calMaxLen(List<Pair<String, List<Pair<Integer, Integer>>>> structs) {
        int maxRow = 0, maxCol = 0;
        for (var struct : structs) {
            List<Pair<Integer, Integer>> poss = struct.getValue();//每一个结构的位置信息
            for (var p : poss) {
                maxRow = Math.max(maxRow, p.getKey());
                maxCol = Math.max(maxCol, p.getValue());
            }
        }
        return new int[]{maxRow + 1, maxCol + 1};
    }

    public static int[] getMaxLen(List<Pair<String, List<Pair<Integer, Integer>>>> structs) {
        if (structs.equals(still)) return getStillMaxLen();
        if (structs.equals(oscillator)) return getOscillatorMaxLen();
        if (structs.equals(fly)) return getFlyMaxLen();
        if (structs.equals(reproduction)) return getReproductionMaxLen();
        if (structs.equals(longLife)) return getLongLifeRowMaxLenMaxLen();
        throw new RuntimeException("未找到对应结构");
    }

}
