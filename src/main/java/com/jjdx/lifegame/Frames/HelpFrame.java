package com.jjdx.lifegame.Frames;

import com.jjdx.lifegame.Plugins.*;
import com.jjdx.lifegame.Structure.Structure;
import com.jjdx.lifegame.Structure.StructureManger;
import com.jjdx.lifegame.Utils.Config;
import com.jjdx.lifegame.Utils.FileUtil;
import com.jjdx.lifegame.Utils.MyLogger;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
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
    int width = Config.getInt("HelpFrame.width"), height = Config.getInt("HelpFrame.height");

    private void initFrame() {
        window = new Stage();
        pane = new Pane();
        Scene newScene = new Scene(pane, width, height);
        try {
            WritableImage icon = ICONer.getIcon(Config.get("global.icon", ""));
            window.getIcons().add(icon);
        } catch (Exception e) {
            MyLogger.warning("HelpFrame - ICON获取出错");
        }
        window.setTitle("帮助");
        window.setScene(newScene);
        window.show();
        window.setOnCloseRequest(e -> instance = null);
    }


    List<Label> bar = new ArrayList<>();
    boolean isShowMain = true;

    /**
     初始化List<Label> bar
     */
    private void initBar() {
        List<Pair<String, EventHandler<MouseEvent>>> list = Arrays.asList(
                new Pair<>("帮助", e -> showMain()),
                new Pair<>("静物", e -> showStruct("still")), // 与resources文件名保持一致
                new Pair<>("震荡器", e -> showStruct("oscillator")),
                new Pair<>("滑翔机", e -> showStruct("fly")),
                new Pair<>("繁殖者", e -> showStruct("reproduction")),
                new Pair<>("寿星", e -> showStruct("longLife")),
                new Pair<>("图形未加载,点击刷新", e -> reloadAndFlush()));
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

    /**
     添加bar: 界面上方按钮
     */
    private void addBar() {
        if (bar.isEmpty()) initBar();
        for (Label label : bar) pane.getChildren().add(label);
    }

    private void copyButton() {
        Label copy = Creator.creatButton("复制到主页", width * 3 / 4, height * 3 / 4, 100, 50);
        copy.setOnMouseClicked(e -> {
            List<int[]> poss = graphs.get(curPage - 1).getValue();
            MainFrame.setStruct(poss);
        });
        pane.getChildren().add(copy);
    }

    /**
     重新加载图形,并刷新
     */
    private void reloadAndFlush() {
        StructureManger.reloadAll();
        if (!isShowMain) showStruct(graphs.getName());
    }

    /**
     主界面展示
     */
    private void showMain() {
        isShowMain = true;
        pane.getChildren().clear();

        pane.setStyle("-fx-background-color: " + Config.getString("HelpFrame.mainBackgroundColor"));
        addBar();
        String info = FileUtil.readFile(FileUtil.findFilePath("info.txt"));
        TextFlow textFlow = new TextFlow();
        for (String line : info.split("\n")) {
            // 创建Text对象
            Text text = new Text(line + "\n");
            if (line.startsWith("①") || line.startsWith("②") || line.startsWith("③")) {
                text.setFont(Font.font(Config.getString("font.name", null), FontWeight.BOLD, Config.getInt("font.size", 20)));
            } else {
                text.setFont(Font.font(Config.getString("font.name", null), Config.getInt("font.size", 20)));
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
    /**
     图像的名称
     */
    Label nameLabel = Creator.creatButton("", 400, 600, 100, 50);
    /**
     当前展示的一种图形
     */
    Structure graphs;

    /**
     展示一种图形

     @param structName 该类图像的名称[
     */
    private void showStruct(String structName) {
        Structure structs = StructureManger.get(structName);
        StructureManger.reload(structName);
        pane.getChildren().clear();
        isShowMain = false;
        pane.setStyle("-fx-background-color: " + Config.getString("HelpFrame.structBackgroundColor"));
        addBar();
        copyButton();
        curPage = 1;
        maxPage = structs.getSize();
        addPage();

        if (structs.isEmpty()) return;
        graphs = structs;
        Pair<String, List<int[]>> first = graphs.getFirst();
        setNameLabelText(first.getKey());
        pane.getChildren().add(nameLabel);

        createMap(structs.getMaxLen());
        drawGraph(first);
    }

    private void setNameLabelText(String name) {
        nameLabel.setText(name);
        nameLabel.setPrefSize(Math.max(100, name.length() * 30), 50);
        nameLabel.setLayoutX((window.getWidth() - nameLabel.getPrefWidth()) / 2);
    }

    /**
     页码数据
     */
    int curPage = 1, maxPage = 0;

    /**
     额外空出的方格,用于美观
     */
    int extraBlock = 6;

    /**
     创建map区域

     @param bound 边界大小[最大行,最大列]
     */
    private void createMap(int[] bound) {
        int row = bound[0] + extraBlock, col = bound[1] + extraBlock;
        map = new Rectangle[row][col];

        double offsetX = (pane.getWidth() - col * (this.len + 1)) / 2, offsetY = (pane.getHeight() - row * (this.len + 1)) / 2;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Rectangle rect = new Rectangle(offsetX + j * (this.len + 1), offsetY + i * (this.len + 1), this.len, this.len);
                rect.setFill(deadColor);
                map[i][j] = rect;
                pane.getChildren().add(rect);
            }
        }
    }

    /**
     绘制图像

     @param graph 要绘制的图像 < 图像名, 位置信息 >
     */
    private void drawGraph(Pair<String, List<int[]>> graph) {
        clearMap();
        setNameLabelText(graph.getKey());
        for (int[] p : graph.getValue()) {
            map[p[0] + extraBlock / 2][p[1] + extraBlock / 2].setFill(liveColor);
        }
    }

    /**
     清空图像
     */
    private void clearMap() {
        for (Rectangle[] rectangles : map) {
            for (Rectangle rectangle : rectangles) {
                rectangle.setFill(deadColor);
            }
        }
    }

    /**
     添加显示页码的label组件
     */
    private void addPage() {
        int y = 700;
        Label pre = Creator.creatButton("<", 650, y, 50, 50);
        Label page = Creator.creatButton("1 / " + maxPage, (int) (pre.getLayoutX() + pre.getPrefWidth()), y, 100, 50);
        Label next = Creator.creatButton(">", (int) (page.getLayoutX() + page.getPrefWidth()), y, 50, 50);
        pre.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changePage(-1, page));
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changePage(1, page));

        pane.getChildren().addAll(page, pre, next);
    }

    /**
     下/上一页

     @param x    1为下一页,-1为上一页
     @param page 显示页码的label组件
     */
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
