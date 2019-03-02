package com.goexp.galgame.gui.util;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class AppCache {

    public static Map<Integer, ObservableList<Game>> gameListCache = new HashMap<>();

    public static Map<Integer, Game> gameCache = new HashMap<>();

    public static Map<Integer, Brand> brandCache = new HashMap<>();

    public static ObservableList<Brand> cacheList;


    public static ImageCache imageCache = new ImageCache();

    public static HashMap<String, Image> localImageCache = new HashMap<>();


//    public static HashMap<String, Image> gameTinyimageCache = new HashMap<>();
//    public static HashMap<String, Image> gameSmallImageCache = new HashMap<>();
//    public static HashMap<String, Image> gameLargeImageCache = new HashMap<>();


//    public static HashMap<String, Image> simpleImageCache = new HashMap<>();
//    public static HashMap<String, Image> simpleLargeImageCache = new HashMap<>();


}
