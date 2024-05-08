package com.jjdx.lifegame.Frames;

import com.jjdx.lifegame.Plugins.Config;
import com.jjdx.lifegame.Plugins.ICONer;
import com.jjdx.lifegame.Plugins.MyLogger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 开机动画
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/7 <br> */
public class AnimationFrame {

    public static void animaion(Stage stage, Callable<Scene> sceneCallable) {
        CompletableFuture<Scene> future = CompletableFuture.supplyAsync(() -> {
            try {
                return sceneCallable.call();//异步执行,拿到返回值 Scene mainFrame
            } catch (Exception e) {
                MyLogger.severe("主窗口加载失败, 程序退出");
                System.exit(-1);
            }
            return null;
        });
        // 动画界面
        Stage animationFrame = new Stage(StageStyle.TRANSPARENT);
        Pane root = new Pane();
        root.setOpacity(Config.get("AnimationFrame.opacity", 1.0));
        root.setStyle("-fx-background-color: " + Config.get("AnimationFrame.backgroundColor", "transparent"));
        int paneWidth = Config.get("AnimationFrame.width", 1400), paneHeight = Config.get("AnimationFrame.height", 820);
        Scene animation = new Scene(root, paneWidth, paneHeight, Color.TRANSPARENT);
        animation.setFill(null);
        ImageView circle = new ImageView(ICONer.getIcon(Config.get("global.icon", "")));
        int r = Config.get("AnimationFrame.radius", 50);
        circle.setFitWidth(r);
        circle.setFitHeight(r);
        circle.setLayoutX((paneWidth - r) / 2.0);
        circle.setLayoutY(paneHeight);
        root.getChildren().add(circle);
        animationFrame.setScene(animation);
        //动画效果
        Timeline timeline = new Timeline();//动画1: 图标旋转放大
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0),
                new KeyValue(circle.translateYProperty(), -animation.getHeight() / 2 - 120),
                new KeyValue(circle.translateXProperty(), -130),
                new KeyValue(circle.rotateProperty(), 360 * 2),
                new KeyValue(circle.fitWidthProperty(), 300 - r),
                new KeyValue(circle.fitHeightProperty(), 300 - r)
        ));
        timeline.setOnFinished(e -> {//动画2:等待
            MyLogger.fine("动画1结束");
            Timeline waitLine = new Timeline();
            waitLine.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4)));
            waitLine.setOnFinished(event -> {
                MyLogger.fine("动画2结束");
                //等待主窗口加载完
                long time = System.currentTimeMillis();
                while (!future.isDone()) {
                    if (System.currentTimeMillis() - time > Config.get("timeout", 8) * 1000) {
                        //打印错误日志
                        MyLogger.severe("主窗口加载超时, 程序退出");
                        System.exit(-1);
                    }
                }
                future.thenAccept(mainFrame -> {//动画结束: 展示主窗口
                    MyLogger.fine("主窗口加载完成, 展示主窗口");
                    animationFrame.close();
                    stage.setScene(mainFrame);
                    stage.show();
                });
            });
            waitLine.play();
        });
        animationFrame.show();
        timeline.play();
        MyLogger.fine("动画开始");
    }
}
