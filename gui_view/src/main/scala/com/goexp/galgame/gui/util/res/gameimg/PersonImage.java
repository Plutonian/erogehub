package com.goexp.galgame.gui.util.res.gameimg;

import com.goexp.galgame.common.website.getchu.GetchuGame;
import com.goexp.galgame.gui.model.Game;
import javafx.scene.image.Image;

public class PersonImage {

    public static Image small(Game game, int index, String src) {
        final var url = GetchuGame.getUrlFromSrc(src);

        return Util.getImage(game, new CacheKey(game.id + "/char_s_" + index, url));
    }
}
