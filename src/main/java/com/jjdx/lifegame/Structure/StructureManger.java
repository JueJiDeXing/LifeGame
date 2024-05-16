package com.jjdx.lifegame.Structure;

import com.jjdx.lifegame.Plugins.FileUtil;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 结构: [静物|震荡器|滑翔机|繁殖者|寿星]
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class StructureManger {
    private StructureManger() {
    }

    static List<String> names = Arrays.asList("still", "oscillator", "fly", "reproduction", "longLife");// 与resources文件名保持一致
    static HashMap<String, Structure> structMap = new HashMap<>();

    static {
        for (String s : names) structMap.put(s, new Structure(s));
    }

    /**
     重新加载图形
     */
    public static void reloadAll() {
        for (Map.Entry<String, Structure> entry : structMap.entrySet()) {
            String name = entry.getKey();
            Structure structure = entry.getValue();
            structure.clear();
            load(structure.struct, name);
        }
    }

    /**
     重新加载图形
     */
    public static void reload(String structName) {
        Structure structure = structMap.get(structName);
        load(structure.struct, structName);
    }

    /**
     加载type类型的图形到structs
     */
    static void load(List<Pair<String, List<int[]>>> structs, String type) {
        structs.clear();
        try {
            String filePath = FileUtil.findFilePath(type + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            br.lines().forEach(line -> {
                String[] split = line.split(" ");
                String name = split[0];
                int[] poss = Arrays.stream(split).skip(1).mapToInt(Integer::parseInt).toArray();
                List<int[]> map = new ArrayList<>();
                for (int i = 0; i < poss.length; i += 2) {
                    map.add(new int[]{poss[i], poss[i + 1]});
                }
                structs.add(new Pair<>(name, map));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int[] getMaxLen(String structName) {
        Structure structure = structMap.get(structName);
        return structure.getMaxLen();
    }

    public static Structure get(String structName) {
        return structMap.get(structName);
    }
}
