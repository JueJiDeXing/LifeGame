module com.jjdx.lifegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.yaml.snakeyaml;


    exports com.jjdx.lifegame.Frames;
    opens com.jjdx.lifegame.Frames to javafx.fxml;
    exports com.jjdx.lifegame.Plugins;
    opens com.jjdx.lifegame.Plugins to javafx.fxml;
}
