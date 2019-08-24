package com.goexp.galgame.gui;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.MainController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

        var proxy = new FXMLLoaderProxy<Parent, MainController>("HGame.fxml");
        primaryStage.setTitle("エロゲ まとめ");

        primaryStage.setWidth(1400);
        primaryStage.setMinWidth(1200);

        primaryStage.setHeight(800);
        primaryStage.setMinHeight(800);
        primaryStage.setScene(new Scene(proxy.node, Color.BLACK));

        primaryStage.show();
    }
}
