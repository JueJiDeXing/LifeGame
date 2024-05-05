package com.jjdx.lifegame.Frames;

import com.jjdx.lifegame.Plugins.Creator;
import com.jjdx.lifegame.Plugins.Loader;
import com.jjdx.lifegame.Plugins.Structure;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 游戏帮助
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class HelpFrame {

    public static HelpFrame instance;//单例模式,防止创建出多个窗口

    private HelpFrame() {
        initFrame();
        showMain();
    }

    public static HelpFrame getInstance() {
        return instance == null ? (instance = new HelpFrame()) : instance;
    }

    Pane pane;
    Stage window;

    private void initFrame() {
        window = new Stage();
        pane = new Pane();
        Scene newScene = new Scene(pane, 900, 800);
        window.getIcons().add(new Image(Loader.findFilePath("mylife.png")));
        window.setTitle("帮助");
        window.setScene(newScene);
        window.show();
        window.setOnCloseRequest(e -> instance = null);
    }


    List<Label> bar = new ArrayList<>();

    /**
     初始化到bar
     */
    private void initBar() {
        List<Pair<String, EventHandler<MouseEvent>>> list = List.of(
                new Pair<>("帮助", e -> showMain()),
                new Pair<>("静物", e -> showStruct(Structure.still)),
                new Pair<>("震荡器", e -> showStruct(Structure.oscillator)),
                new Pair<>("滑翔机", e -> showStruct(Structure.fly)),
                new Pair<>("繁殖者", e -> showStruct(Structure.reproduction)),
                new Pair<>("寿星", e -> showStruct(Structure.longLife)),
                new Pair<>("图形未加载,点击刷新", e -> reload()),
                new Pair<>("----测试按钮----", e -> reload())
        );
        int itemPreWidth = 100, itemPreHeight = 50, itemX = 0, itemY = 0;// 横向排列按钮
        for (var s : list) {
            String name = s.getKey();
            int prefW = Creator.calPrefW(name, itemPreWidth);
            if (itemX + prefW > window.getWidth()) {//这行放不下了,换行
                if (prefW > window.getWidth()) continue;//太长了,放不了
                itemX = 0;
                itemY += itemPreHeight + 5;
            }
            Label label = Creator.creatButton(name, itemX, itemY, itemPreWidth, itemPreHeight);
            itemX += (int) (label.getPrefWidth() + 5);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, s.getValue());
            bar.add(label);
        }
    }

    private void addBar() {
        if (bar.isEmpty()) initBar();
        for (Label label : bar) pane.getChildren().add(label);
    }

    private void reload() {
        Structure.reload();
    }

    /**
     主界面展示
     */
    private void showMain() {
        pane.setStyle("-fx-background-color: #ffffff");
        pane.getChildren().clear();
        addBar();
        String info = Loader.readFile(Loader.findFilePath("info.txt"));
        TextFlow textFlow = new TextFlow();
        for (String line : info.split("\n")) {
            // 创建Text对象
            Text text = new Text(line + "\n");
            if (line.startsWith("①") || line.startsWith("②") || line.startsWith("③")) {
                text.setFont(Font.font("FangSong", FontWeight.BOLD, 20));
            } else {
                text.setFont(Font.font("FangSong", 20));
            }
            textFlow.getChildren().add(text);
        }
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        //textFlow.setEditable(false);
        textFlow.setLayoutX(120);
        textFlow.setLayoutY(120);
        textFlow.setPrefSize(600, 600);
        pane.getChildren().add(textFlow);
    }

    public int len = 15;//方格的行列数和每块的边长
    public Rectangle[][] map;//方格
    public Color liveColor = Color.WHITE, deadColor = Color.BLACK;//细胞的死活颜色

    Label nameLabel = Creator.creatButton("", 400, 600, 100, 50);
    List<Pair<String, List<Pair<Integer, Integer>>>> graphs;

    private void showStruct(List<Pair<String, List<Pair<Integer, Integer>>>> structs) {
        pane.setStyle("-fx-background-color: #129d4f");
        pane.getChildren().clear();
        addBar();

        curPage = 1;
        maxPage = structs.size();
        addPage();

        if (structs.isEmpty()) return;
        graphs = structs;
        Pair<String, List<Pair<Integer, Integer>>> first = graphs.get(0);
        setNameLabelText(first.getKey());
        pane.getChildren().add(nameLabel);

        createMap(Structure.getMaxLen(structs));
        drawGraph(first);
    }

    private void setNameLabelText(String name) {
        nameLabel.setText(name);
        nameLabel.setPrefSize(Math.max(100, name.length() * 30), 50);
        nameLabel.setLayoutX((window.getWidth() - nameLabel.getPrefWidth()) / 2);
    }


    int curPage = 1, maxPage = 0;
    int extraBlock = 6;

    private void createMap(int[] bound) {
        System.out.println(bound[0] + " " + bound[1]);
        int row = bound[0] + extraBlock, col = bound[1] + extraBlock;
        map = new Rectangle[row][col];

        double offsetX = (pane.getWidth() - col * (this.len + 1)) / 2,
                offsetY = (pane.getHeight() - row * (this.len + 1)) / 2;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Rectangle rect = new Rectangle(
                        offsetX + j * (this.len + 1), offsetY + i * (this.len + 1), this.len, this.len);
                rect.setFill(deadColor);
                map[i][j] = rect;
                pane.getChildren().add(rect);
            }
        }
    }

    private void drawGraph(Pair<String, List<Pair<Integer, Integer>>> graph) {
        clearMap();
        setNameLabelText(graph.getKey());
        for (Pair<Integer, Integer> p : graph.getValue()) {
            map[p.getKey() + extraBlock / 2][p.getValue() + extraBlock / 2].setFill(liveColor);
        }
    }

    private void clearMap() {
        for (Rectangle[] rectangles : map) {
            for (Rectangle rectangle : rectangles) {
                rectangle.setFill(deadColor);
            }
        }
    }


    private void addPage() {
        int y = 700;
        Label pre = Creator.creatButton("<", 650, y, 50, 50);
        Label page = Creator.creatButton("1 / " + maxPage, (int) (pre.getLayoutX() + pre.getPrefWidth()), y, 100, 50);
        Label next = Creator.creatButton(">", (int) (page.getLayoutX() + page.getPrefWidth()), y, 50, 50);
        pre.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changePage(-1, page));
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changePage(1, page));

        pane.getChildren().addAll(page, pre, next);
    }

    private void changePage(int x, Label page) {
        if (curPage + x < 1 || curPage + x > maxPage) return;
        curPage += x;
        page.setText(curPage + " / " + maxPage);
        drawGraph(graphs.get(curPage - 1));
    }

    public void showWindow() {
        //窗口置为顶层
        window.setAlwaysOnTop(true);
        window.setAlwaysOnTop(false);
    }
}
