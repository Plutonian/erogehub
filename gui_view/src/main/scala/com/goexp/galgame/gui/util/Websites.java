package com.goexp.galgame.gui.util;

import com.goexp.galgame.gui.HGameApp;

public class Websites {

    public static void open(String url) {
        HGameApp.app.getHostServices().showDocument(url);
    }
}
