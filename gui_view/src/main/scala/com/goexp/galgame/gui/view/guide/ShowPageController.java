package com.goexp.galgame.gui.view.guide;

import com.goexp.galgame.gui.view.DefaultController;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebView;

public class ShowPageController extends DefaultController {


    @FXML
    private WebView webView;


    /**
     * Event
     */


    protected void initialize() {
        webView.setFontSmoothingType(FontSmoothingType.GRAY);


        /**
         * WebView setting
         */


        webView
                .getEngine().getLoadWorker().messageProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Mess:" + newValue);

        });

        webView
                .getEngine().getLoadWorker().exceptionProperty().addListener((ov, t, t1) -> {
            if (t1 != null)
                System.out.println("Received exception: " + t1.getMessage());
        });

        webView
                .getEngine()
                .getLoadWorker()
                .stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        System.out.println("SUCCEEDED");
                    } else if (newValue == Worker.State.RUNNING) {
                        System.out.println("RUNNING");
                    } else if (newValue == Worker.State.READY) {
                        System.out.println("READY");
                    } else if (newValue == Worker.State.SCHEDULED) {
                        System.out.println("SCHEDULED");
                    } else if (newValue == Worker.State.FAILED) {
                        System.out.println("FAILED");
                    }
                }); // addListener()

    }

    public void load(String html) {
        webView
                .getEngine().loadContent(html);
    }

}
