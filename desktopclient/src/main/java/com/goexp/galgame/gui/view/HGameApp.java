package com.goexp.galgame.gui.view;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * A sample that demonstrates a WebView object accessing a web page.
 */
public class HGameApp extends Application {
    public static HGameApp app;

    public static void main(String[] args) {

        Application.launch();
    }

    @Override
    public void init() {
        app = this;
        Network.initProxy();
    }

    @Override
    public void start(Stage primaryStage) {

        Parent root = new FXMLLoaderProxy("HGame.fxml").load();
        primaryStage.setTitle("エロゲ まとめ");

        primaryStage.setWidth(1400);
        primaryStage.setMinWidth(1200);
//        primaryStage.setMaxWidth(1280);

        primaryStage.setHeight(800);
        primaryStage.setMinHeight(800);
        primaryStage.setScene(new Scene(root, Color.BLACK));

        primaryStage.show();
    }
}
