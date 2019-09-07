package com.goexp.galgame.gui.util;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class FXMLLoaderProxy<N, C> {

    public final N node;
    public final C controller;
    private final FXMLLoader loader;

    public FXMLLoaderProxy(String path) {
        this(FXMLLoaderProxy.class.getClassLoader().getResource(path));
    }

    public FXMLLoaderProxy(URL url) {
        this.loader = new FXMLLoader(url);

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
