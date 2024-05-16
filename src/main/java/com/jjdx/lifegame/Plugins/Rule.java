package com.jjdx.lifegame.Plugins;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.jjdx.lifegame.Plugins.Util.eightDir;
import static com.jjdx.lifegame.Plugins.Util.isValid;

/**
 生命规则
 <br>
 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/16 <br>
 */
public class Rule {
    /**
     更新一步状态
     */
  public static List<int[]> update(Rectangle[][] map,Color liveColor) {
        int row = map.length, col = map[0].length;
        List<int[]> needUpdate = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int cnt = 0;
                for (int[] d : eightDir) {
                    int nI = i + d[0], nJ = j + d[1];
                    if (!isValid(nI, nJ, row, col)) continue;
                    if (isLive(map,nI, nJ,liveColor)) cnt++;
                }
                if (isLive(map,i, j,liveColor)) {//活细胞
                    // 孤独/拥挤
                    if (cnt <= 1 || 4 <= cnt) needUpdate.add(new int[]{i, j});
                } else {//死细胞,繁衍
                    if (cnt == 3) needUpdate.add(new int[]{i, j});
                }
            }
        }
        return needUpdate;
    }
    static   boolean isLive(Rectangle[][] map, int i, int j, Color liveColor) {
        return map[i][j].getFill() == liveColor;
    }

}
