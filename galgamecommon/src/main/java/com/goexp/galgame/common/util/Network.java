package com.goexp.galgame.common.util;


import com.goexp.galgame.common.Config;

import java.util.ResourceBundle;

public class Network {
    private static boolean isInit = false;

    public static void initProxy() {

        if (Config.proxy && !isInit) {

            var prop = ResourceBundle.getBundle("proxy");
            prop.keySet().forEach(key -> {
                System.setProperty(key, prop.getString(key));
            });

            isInit = true;
        }
    }
}
