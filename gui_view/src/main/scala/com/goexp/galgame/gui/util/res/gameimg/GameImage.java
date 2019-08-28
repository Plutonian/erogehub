package com.goexp.galgame.gui.util.res.gameimg;

import com.goexp.galgame.common.website.getchu.GetchuGame;
import com.goexp.galgame.gui.model.Game;
import javafx.scene.image.Image;

public class GameImage {

    public static Image tiny(Game game) {
        return Util.getImage(game, new CacheKey(game.id + "/game_t", game.smallImg));
    }

    public static Image small(Game game) {
        final var url = GetchuGame.SmallImg(game.id);

        return Util.getImage(game, new CacheKey(game.id + "/game_s", url));
    }

    public static Image large(Game game) {
        final var url = GetchuGame.LargeImg(game.id);

        return Util.getImage(game, new CacheKey(game.id + "/game_l", url));
    }

    public static void preloadLarge(Game game) {
        final var url = GetchuGame.LargeImg(game.id);

        Util.preLoadRemoteImage(new CacheKey(game.id + "/game_l", url));
    }

}
