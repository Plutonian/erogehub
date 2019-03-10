package com.goexp.galgame.common;

import java.nio.file.Path;
import java.util.ResourceBundle;

public class Config {

    static {

        //load config
        var prop = ResourceBundle.getBundle("config");

        DATA_ROOT = Path.of(prop.getString("DATA_ROOT"));
    }

    public static boolean proxy = true;

    public static final Path DATA_ROOT;
}
