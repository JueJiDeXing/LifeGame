package com.jjdx.lifegame.Plugins;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

/**
 圆角按钮建造器
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class Creator {
    private Creator() {
    }

    public static int calPrefW(String text, int w) {
        return Math.max(text.length() * 25, w);
    }

    /**
     创建圆角按钮
     */
    public static Label creatButton(String text, int x, int y, int w, int h) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefSize(calPrefW(text, w), h);
        label.setFont(Font.font(Config.getString("font.name",null), Config.getInt("font.size",20)));
        label.setTextFill(Color.BLACK);
        label.setBackground(new Background(new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    /**
     创建圆角按钮
     */
    public static Label creatButton(String text, int x, int y, int w, int h, int c) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefSize(calPrefW(text, w), h);
        label.setFont(Font.font(Config.getString("font.name",null), Config.getInt("font.size",20)));
        label.setTextFill(Color.BLACK);
        label.setBackground(new Background(new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(c), new BorderWidths(2))));
        label.setAlignment(Pos.CENTER);
        return label;
    }

}
