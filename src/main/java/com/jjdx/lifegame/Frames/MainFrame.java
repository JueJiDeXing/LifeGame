package com.jjdx.lifegame.Frames;

import com.jjdx.lifegame.Plugins.*;
import com.jjdx.lifegame.Utils.Config;
import com.jjdx.lifegame.Utils.FileUtil;
import com.jjdx.lifegame.Utils.MyLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jjdx.lifegame.Utils.Util.isValid;
import static com.jjdx.lifegame.Utils.Util.sleep;

/**
 生命游戏主页面
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/4/28 <br> */
public class MainFrame extends Application {
    public static void main(String[] args) {
        MyLogger.info(new SimpleDateFormat("M月d日 E HH:mm").format(new Date()) + " -- 开始运行");
        launch();
    }

    Pane rootPane;
    Stage primaryStage;
    int width, height;
    int fact = 1;//加速因子
    boolean isStart = false;//生命是否为运行状态
    int offsetX, offsetY;//方格的偏移量
    public int row, col, len;//方格的行列数和每块的边长
    public static Rectangle[][] map;//方格
    public static Color liveColor;
    public static Color deadColor;//细胞的死活颜色
    static int liveCnt = 0;//存活数量
    static Label liveText;//存活数量显示


    @Override
    public void start(Stage stage) {
        AnimationFrame.animaion(stage, () -> {
            loadConfig();
            MyLogger.fine("主窗口已读入配置信息");
            Scene scene = new Scene((rootPane = new Pane()), width, height);
            initStage(stage);
            MyLogger.fine("stage初始化完毕");
            initButton();
            initLiveText();
            initGrid();
            setInitialSituation();
            MyLogger.fine("主窗口初始化完毕");
            return scene;
        });
    }

    private void loadConfig() {
        width = Config.get("MainFrame.width", 0);
        height = Config.get("MainFrame.height", 0);
        offsetX = Config.get("board.offsetX", 0);
        offsetY = Config.get("board.offsetY", 0);//方格的偏移量
        row = Config.get("board.row", 0);
        col = Config.get("board.col", 0);
        len = Config.get("board.len", 0);//方格的行列数和每块的边长
        liveColor = Config.get("board.liveColor", Color.WHITE);
        deadColor = Config.get("board.deadColor", Color.BLACK);

    }

    /**
     初始化界面信息
     */
    private void initStage(Stage stage) {
        primaryStage = stage;
        rootPane.setStyle("-fx-background-color: " + Config.getString("MainFrame.backgroundColor"));
        rootPane.setOnScroll(this::scale);
        stage.setTitle("生命游戏");
        stage.setOnCloseRequest(event -> {
            MyLogger.off(new SimpleDateFormat("M月d日 E HH:mm").format(new Date()) + " -- 主窗口已关闭");
        });
        try {
            WritableImage icon = ICONer.getIcon(Config.get("global.icon", ""));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            MyLogger.warning("MainFrame#initStage - 图标获取出错  Config.get=" + Config.get("global.icon", ""));
        }
    }

    /**
     添加网格
     */
    private void initGrid() {
        map = new Rectangle[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Rectangle rect = new Rectangle(offsetX + j * (len + 1), offsetY + i * (len + 1), len, len);
                rect.setFill(deadColor);
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClick);
                rootPane.getChildren().add(rect);
                map[i][j] = rect;
            }
        }
    }

    /**
     设置初始态
     */
    private void setInitialSituation() {
        String struct = FileUtil.readFile(FileUtil.findFilePath("InitialSituation.txt"));
        if (struct.isEmpty()) return;
        String[] split = struct.split(" ");
        List<int[]> poss = new ArrayList<>();
        for (int i = 0; i < split.length; i += 2) {
            try {
                int r = Integer.parseInt(split[i]), c = Integer.parseInt(split[i + 1]);
                poss.add(new int[]{r, c});
            } catch (Exception e) {
                MyLogger.fine("错误坐标:" + split[i] + " " + split[i + 1]);
            }
        }
        setStruct(poss);
    }

    public static void setStruct(List<int[]> poss) {
        clearMap();
        for (int[] pos : poss) {
            if (!isValid(pos, map.length, map[0].length)) {
                MyLogger.fine("坐标越界:" + Arrays.toString(pos));
                continue;
            }
            map[pos[0]][pos[1]].setFill(liveColor);
            liveCnt++;
        }
    }

    int buttonStartX = 1000, buttonStartY = 60, buttonWidth = 100, buttonHeight = 80;
    int space = 100, spaceId = 0;

    int[] getButtonPos() {//按钮按列布局
        int y = buttonStartY + space * (spaceId++);
        if (y + buttonHeight + 50 > rootPane.getHeight()) {//双列
            buttonStartX += 180;
            spaceId = 0;
            return getButtonPos();
        }
        return new int[]{buttonStartX, y};
    }

    /**
     添加按钮
     */
    private void initButton() {
        List<Pair<String, EventHandler<MouseEvent>>> textWithMethod = Arrays.asList(
                new Pair<>("开始/停止", e -> startOrStopGame()),
                new Pair<>("执行一步", e -> runGame()),
                new Pair<>("回退一步", e -> backGame()),
                new Pair<>("清空图像", e -> clearMap()),
                new Pair<>("平移图像", e -> moveMap()),
                new Pair<>("玩法帮助", e -> getHelp()),
                new Pair<>("速度×" + fact, this::changeSpeed),
                new Pair<>("添加为静物", e -> printFile("still")),
                new Pair<>("添加为震荡", e -> printFile("oscillator")),
                new Pair<>("添加为飞行器", e -> printFile("fly")),
                new Pair<>("添加为繁殖", e -> printFile("reproduction")),
                new Pair<>("添加为寿星", e -> printFile("longLife")),
                new Pair<>("保存到文件", e -> saveFile()),
                new Pair<>("从文件加载", e -> loadFile()));
        for (var textAndFunc : textWithMethod) {
            String text = textAndFunc.getKey();
            int[] buttonPos = getButtonPos();
            Label label = Creator.creatButton(text, buttonPos[0], buttonPos[1], buttonWidth, buttonHeight);//按钮按列布局
            EventHandler<MouseEvent> func = textAndFunc.getValue();
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, func);
            rootPane.getChildren().add(label);
        }
    }


    /**
     将图像移动rowMove和colMove个单位
     */
    void moveMap(int rowMove, int colMove) {
        List<int[]> needUpdate = new ArrayList<>();//需要改变状态的位置
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (!isValid(i - rowMove, j - colMove, row, col)) {//越界了,默认为死细胞
                    if (isLive(i, j)) needUpdate.add(new int[]{i, j});
                    continue;
                }
                if (isLive(i - rowMove, j - colMove) != isLive(i, j)) {
                    needUpdate.add(new int[]{i, j});
                }
            }
        }
        for (int[] p : needUpdate) {
            changeCellStatus(map[p[0]][p[1]]);
        }
    }

    /**
     将图像数据保存到文件
     */
    void write(String name, String filePath) {
        StringBuilder text = new StringBuilder();
        text.append(name).append(" ");
        List<int[]> poss = new ArrayList<>();
        int minI = Integer.MAX_VALUE, minJ = Integer.MAX_VALUE;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (!isLive(i, j)) continue;
                poss.add(new int[]{i, j});
                minI = Math.min(minI, i);
                minJ = Math.min(minJ, j);
            }
        }
        for (int[] p : poss) text.append((p[0] - minI)).append(" ").append(p[1] - minJ).append(" ");
        FileUtil.writeFile(filePath, text.toString());
    }


    private void initLiveText() {
        liveText = new Label("当前存活: " + liveCnt);
        liveText.setLayoutX(offsetX);
        liveText.setLayoutY(offsetY - 40);
        liveText.setFont(new Font(Config.getString("font.name"), Config.getInt("font.size", 20)));
        liveText.setTextFill(Color.WHITE);
        rootPane.getChildren().add(liveText);
    }

    private static void setLiveText(int live) {
        liveCnt = live;
        liveText.setText("当前存活: " + liveCnt);
    }

    /**
     方格被点击
     */
    private void onClick(MouseEvent e) {
        Achievement.alertAchievement(true, Achievement.Achieve.God);
        Rectangle rect = (Rectangle) e.getSource();
        changeCellStatus(rect);
        liveText.setText("当前存活: " + liveCnt);
    }

    /**
     切换细胞状态,并维护存活数量
     */
    void changeCellStatus(Rectangle rect) {
        if (rect.getFill() == liveColor) {
            rect.setFill(deadColor);
            liveCnt--;
        } else {
            rect.setFill(liveColor);
            liveCnt++;
        }
    }

    boolean isLive(int i, int j) {
        return map[i][j].getFill() == liveColor;
    }

    /**
     开始/停止
     */
    void startOrStopGame() {
        if (isStart) {
            isStart = false;
            return;
        }
        isStart = true;
        new Thread(() -> {
            while (isStart) {
                update();
                Platform.runLater(() -> Achievement.alertAchievement(liveCnt == 0, Achievement.Achieve.BrokenLife));
                Platform.runLater(() -> Achievement.alertAchievement(liveCnt >= 100, Achievement.Achieve.OneHundred));
                sleep(1000 / fact);
            }
        }).start();
    }

    /**
     更新一步状态
     */
    void update() {
        List<int[]> needUpdate = Rule.update(map, liveColor);
        Platform.runLater(() -> Achievement.alertAchievement(needUpdate.isEmpty(), Achievement.Achieve.Stable));

        for (int[] p : needUpdate) {
            changeCellStatus(map[p[0]][p[1]]);
        }
        Platform.runLater(() -> liveText.setText("当前存活: " + liveCnt));
    }

    /**
     执行一步
     */
    void runGame() {
        update();
    }

    /**
     回退一步
     */
    void backGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("生命无法回头, 请继续前进吧");
        alert.showAndWait();
    }

    /**
     清空活细胞
     */
    private static void clearMap() {
        for (Rectangle[] rects : map) {
            for (Rectangle rect : rects) {
                rect.setFill(deadColor);
            }
        }
        setLiveText(0);
    }

    /**
     移动图像
     */
    private void moveMap() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("输入");
        //输入框
        TextField inputRow = new TextField(), inputCol = new TextField();
        inputRow.setPromptText("请输入平移行数");
        inputCol.setPromptText("请输入平移列数");
        HBox inputRowHBox = new HBox(inputRow, inputCol);
        alert.getDialogPane().setContent(inputRowHBox);
        //按钮
        ButtonType okButton = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == okButton) {
            if (inputRow.getText().isEmpty() || inputCol.getText().isEmpty()) return;
            try {
                int rowMove = Integer.parseInt(inputRow.getText()), colMove = Integer.parseInt(inputCol.getText());
                moveMap(rowMove, colMove);
            } catch (Exception ignored) {

            }
        }
    }

    /**
     帮助弹窗
     */
    private void getHelp() {
        HelpFrame.getInstance().showWindow();
    }

    /**
     点击按钮,切换速度
     */
    private void changeSpeed(MouseEvent e) {
        Label speed = (Label) e.getSource();
        fact = 2 * fact % 31;// fact=32 -> fact=1
        speed.setText("速度×" + fact);
    }

    /**
     输出到文件
     */
    private void printFile(String fileName) {
        //弹窗
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("输入");
        alert.setHeaderText("请命名");
        //输入框
        TextField input = new TextField();
        input.setPromptText("请输入名称");
        alert.getDialogPane().setContent(input);
        //按钮
        ButtonType okButton = new ButtonType("确定");
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == okButton) write(input.getText(), FileUtil.findFilePath(fileName));
        });

    }

    /**
     将图像保存到选择的文件
     */
    private void saveFile() {
        //弹出文件选择框
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("文本文件(*.txt)", "*.txt"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null) return;
        //写入
        write(file.getName(), file.getPath());
    }

    /**
     从选择的文件加载图像
     */
    private void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("文本文件(*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) return;
        try {
            //读取
            BufferedReader br = new BufferedReader(new FileReader(file));
            String[] info = br.readLine().split(" ");//只有一行 [name , ...pos]
            if (info.length % 2 != 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("错误文件,坐标数量不成对");
                alert.showAndWait();
                return;
            }
            clearMap();
            setLiveText(info.length / 2);
            for (int i = 1; i < info.length; i += 2) {
                int r = Integer.parseInt(info[i]), c = Integer.parseInt(info[i + 1]);
                map[r][c].setFill(liveColor);
            }
        } catch (Exception e) {
            MyLogger.warning("图像文件读取失败:" + e.getMessage());
        }
    }

    Tick scaleTick = new Tick();// 防抖

    /**
     缩放界面
     */
    private void scale(ScrollEvent e) {
        if (scaleTick.distance() < 200) return;// 防抖
        scaleTick.setNow();
        double deltaY = e.getDeltaY();
        if (deltaY > 0) {
            if (len >= 50) return;
            len++;
        } else {
            if (len <= 4) return;
            len--;
        }
        System.out.println(len);
        List<int[]> pos = removeMap();
        col = 880 / (len + 1);
        row = 720 / (len + 1);
        scaleGrid(pos);
    }

    private List<int[]> removeMap() {
        List<int[]> live = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                rootPane.getChildren().remove(map[i][j]);
                if (isLive(i, j)) live.add(new int[]{i, j});
            }
        }
        return live;
    }

    private void scaleGrid(List<int[]> pos) {
        map = new Rectangle[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Rectangle rect = new Rectangle(offsetX + j * (len + 1), offsetY + i * (len + 1), len, len);
                rect.setFill(deadColor);
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClick);
                rootPane.getChildren().add(rect);
                map[i][j] = rect;
            }
        }
        for (int[] p : pos) {
            if (p[0] < row && p[1] < col) map[p[0]][p[1]].setFill(liveColor);
        }
    }
}
