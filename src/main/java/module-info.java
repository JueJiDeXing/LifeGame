module com.jjdx.lifegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.graphics;
    requires java.logging;

    exports com.jjdx.lifegame.Frames;
    opens com.jjdx.lifegame.Frames to javafx.fxml;
    exports com.jjdx.lifegame.Plugins;
    opens com.jjdx.lifegame.Plugins to javafx.fxml;
    exports com.jjdx.lifegame.Structure;
    opens com.jjdx.lifegame.Structure to javafx.fxml;
    exports com.jjdx.lifegame.Utils;
    opens com.jjdx.lifegame.Utils to javafx.fxml;
}
