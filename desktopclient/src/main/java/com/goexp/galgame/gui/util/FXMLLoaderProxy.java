package com.goexp.galgame.gui.util;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FXMLLoaderProxy {

    private final FXMLLoader loader;

    public FXMLLoaderProxy(final String path) {
        this.loader = new FXMLLoader(getClass().getClassLoader().getResource(path));
    }

    public <T> T load() {
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getController() {
        return loader.getController();
    }

}
