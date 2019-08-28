package com.goexp.galgame.gui.util.res.gameimg;

import com.goexp.galgame.common.website.getchu.GetchuGame;
import com.goexp.galgame.gui.model.Game;
import javafx.scene.image.Image;

public class SimpleImage {

    public static Image small(Game game, int index, String src) {
        final var url = GetchuGame.smallSimpleImg(src);

        return Util.getImage(game, new CacheKey(game.id + "/simple_s_" + index, url));
    }

    public static Image large(Game game, int index, String src) {
        final var url = GetchuGame.largeSimpleImg(src);

        return Util.getImage(game, new CacheKey(game.id + "/simple_l_" + index, url));
    }
}
