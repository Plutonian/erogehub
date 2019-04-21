package com.goexp.galgame.gui.util;

import com.goexp.galgame.gui.model.Brand;

import java.util.HashMap;
import java.util.Map;

public class AppCache {

    public static Map<Integer, Brand> brandCache = new HashMap<>();

    public static ImageMemCache imageMemCache = new ImageMemCache();

}
