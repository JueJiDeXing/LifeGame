package com.jjdx.lifegame.Plugins;

import javafx.scene.control.Alert;

/**
 成就
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class Achievement {
    private Achievement() {
    }

    public enum Achieve {
        BrokenLife,
        OneHundred,
        God,
        Stable
    }

    public static boolean BrokenLife = false;
    public static boolean OneHundred = false;
    public static boolean God = false;
    public static boolean Stable = false;

    public static void alertAchievement(boolean condition, Achieve achieve) {
        if (!condition) return;
        String header = "", content = "";
        switch (achieve) {
            case BrokenLife: {
                if (BrokenLife) return;
                header = "达成成就--破碎的人生!";
                content = "天定弄人...";
                BrokenLife = true;
                break;
            }
            case OneHundred: {
                if (OneHundred) return;
                header = "达成成就--完美数!";
                content = "存活数达到100,这片土地充满着生机!";
                OneHundred = true;
                break;
            }
            case Stable: {
                if (Stable) return;
                header = "你已获得成就--稳态!";
                content = "你获得了一个稳定的世界!";
                Stable = true;
                break;
            }
            case God: {
                if (God) return;
                header = "你已获得成就--造物主!";
                content = "你会让我们活下去的对吗?";
                God = true;
                break;
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("成就");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
