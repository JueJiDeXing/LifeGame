module com.jjdx.lifegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.yaml.snakeyaml;
    requires image4j;
    requires java.desktop;
    requires javafx.swing;

    exports com.jjdx.lifegame.Frames;
    opens com.jjdx.lifegame.Frames to javafx.fxml;
    exports com.jjdx.lifegame.Plugins;
    opens com.jjdx.lifegame.Plugins to javafx.fxml;
    exports com.jjdx.lifegame.Structure;
    opens com.jjdx.lifegame.Structure to javafx.fxml;
}
