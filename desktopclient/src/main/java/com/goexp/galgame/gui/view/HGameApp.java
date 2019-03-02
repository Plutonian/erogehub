package com.goexp.galgame.gui.view;

import com.goexp.galgame.common.Config;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

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

        if (Config.proxy) {

            // HTTP 代理，只能代理 HTTP 请求

            var prop = ResourceBundle.getBundle("proxy");
            prop.keySet().forEach(key -> {
                System.setProperty(key, prop.getString(key));
            });


            // SOCKS 代理，支持 HTTP 和 HTTPS 请求
            // 注意：如果设置了 SOCKS 代理就不要设 HTTP/HTTPS 代理
//        System.setProperty("socksProxyHost", "127.0.0.1");
//        System.setProperty("socksProxyPort", "53758");

        }

    }

    @Override
    public void start(Stage primaryStage) throws IOException {

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
