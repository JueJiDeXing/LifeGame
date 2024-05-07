package com.jjdx.lifegame.Frames;

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
 开机动画<br>
 TODO 内容写死,待后续修改
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/7 <br> */
public class AnimationFrame {

    public static void animaion(Stage stage, Callable<Scene> sceneCallable) {
        CompletableFuture<Scene> future = CompletableFuture.supplyAsync(() -> {
            try {
                return sceneCallable.call();//异步执行,拿到返回值 Scene mainFrame
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // 动画界面
        Stage animationFrame = new Stage(StageStyle.TRANSPARENT);
        Pane root = new Pane();
        root.setOpacity(1);
        root.setStyle("-fx-background-color: transparent;");
        int paneWidth = 1200, paneHeight = 800;
        Scene animation = new Scene(root, paneWidth, paneHeight, Color.TRANSPARENT);
        animation.setFill(null);
        ImageView circle = new ImageView("file:src/main/resources/images/mylife.png");
        int r = 50;
        circle.setFitWidth(r);
        circle.setFitHeight(r);
        circle.setLayoutX((paneWidth - r) / 2.0);
        circle.setLayoutY(paneHeight);
        root.getChildren().add(circle);
        animationFrame.setScene(animation);
        //动画效果
        Timeline timeline = new Timeline();//动画1: 图标旋转放大
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5),
                new KeyValue(circle.translateYProperty(), -animation.getHeight() / 2 - 100),
                new KeyValue(circle.translateXProperty(), -80),
                new KeyValue(circle.rotateProperty(), 360 * 2),
                new KeyValue(circle.fitWidthProperty(), 300 - r),
                new KeyValue(circle.fitHeightProperty(), 300 - r)
        ));
        timeline.setOnFinished(e -> {//动画2:等待
            Timeline waitLine = new Timeline();
            waitLine.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5)));
            waitLine.setOnFinished(event -> future.thenAccept(mainFrame -> {//动画结束: 展示主窗口

                stage.setScene(mainFrame);
                stage.show();
                animationFrame.close();
            }));
            waitLine.play();
        });
        animationFrame.show();
        timeline.play();
    }
}
