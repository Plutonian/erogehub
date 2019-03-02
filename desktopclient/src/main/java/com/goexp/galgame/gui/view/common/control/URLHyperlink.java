package com.goexp.galgame.gui.view.common.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class URLHyperlink extends Hyperlink {

    private SimpleStringProperty href = new SimpleStringProperty("");

    public String getHref() {
        return href.get();
    }

    public SimpleStringProperty hrefProperty() {
        return href;
    }

    public void setHref(String href) {
        this.href.set(href);
    }

    public URLHyperlink() {
        this("");
    }

    public URLHyperlink(String text, String url) {
        this(text);
        this.setHref(url);
    }


    public URLHyperlink(String text) {
        super(text);

        init();
    }

    private void init() {
        this.setEventHandler(ActionEvent.ACTION, (e) -> {
            try {
                Desktop.getDesktop().browse(new URI(getHref()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });
    }


}