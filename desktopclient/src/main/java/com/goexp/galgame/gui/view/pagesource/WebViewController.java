package com.goexp.galgame.gui.view.pagesource;

import com.goexp.galgame.gui.model.Brand;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebView;

public class WebViewController {

    /**
     * UI Com
     */
    @FXML
    private TextField locationField;

    @FXML
    private Label lbUrl;

    @FXML
    private WebView webView;

    private Brand brand;

    private Service<Void> updateBrandService = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new UpdateBrandWebsiteTask(brand);
        }
    };

    /**
     * Event
     */

    @FXML
    private void initialize() {
        webView.setFontSmoothingType(FontSmoothingType.GRAY);


        /**
         * WebView setting
         */

//        webView.getEngine().setConfirmHandler(new Callback<String, Boolean>() {
//            @Override
//            public Boolean call(String param) {
//                return null;
//            }
//        });

//        WebConsoleListener.setDefaultListener (new WebConsoleListener () {
//            @Override
//            public void messageAdded (WebView webView, String message, int lineNumber, String sourceId) {
//                System.out.println ("Console: [" + sourceId + ":" + lineNumber + "] " + message);
//            }
//        });

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


        lbUrl.textProperty().bind(webView.getEngine().locationProperty());
        locationField.textProperty().bind(webView.getEngine().locationProperty());
    }

    public void load(Brand brand) {
        this.brand = brand;
        webView
                .getEngine().load(brand.website);
    }

    @FXML
    private void goButton_OnAction(ActionEvent actionEvent) {

        brand.website = locationField.getText();
        updateBrandService.restart();
    }

}
