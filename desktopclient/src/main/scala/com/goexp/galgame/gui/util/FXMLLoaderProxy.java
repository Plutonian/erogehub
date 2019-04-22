package com.goexp.galgame.gui.util;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FXMLLoaderProxy<N, C> {

    public final N node;
    public final C controller;
    private final FXMLLoader loader;

    public FXMLLoaderProxy(final String path) {
        this.loader = new FXMLLoader(getClass().getClassLoader().getResource(path));

        node = load();
        controller = loader.getController();
    }

    private N load() {
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
