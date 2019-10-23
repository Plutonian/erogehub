package com.goexp.galgame.gui.view.pagesource;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebView;

public class WebViewController extends DefaultController {


    @FXML
    private TextField locationField;

    @FXML
    private Label lbUrl;

    @FXML
    private WebView webView;



    protected void initialize() {
        webView.setFontSmoothingType(FontSmoothingType.GRAY);


        webView
                .getEngine().getLoadWorker().messageProperty().addListener((observable, oldValue, newValue) -> System.out.println("Mess:" + newValue));

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
                    System.out.println(newValue);
                }); // addListener()


        lbUrl.textProperty().bind(webView.getEngine().locationProperty());
        locationField.textProperty().bind(webView.getEngine().locationProperty());
    }

    public void load(Brand brand) {
        webView
                .getEngine().load(brand.website());
    }

    @FXML
    private void goButton_OnAction(ActionEvent actionEvent) {

    }

}
