package com.goexp.galgame.data.model;

import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.common.model.GameState;

import java.util.Objects;

public class Game extends CommonGame {


    public GameState state;

    public int brandId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return id == game.id &&
//                Objects.equals(name, game.name) &&
//                Objects.equals(publishDate, game.publishDate) &&
//                Objects.equals(imgUrl, game.imgUrl) &&
//                Objects.equals(website, game.website) &&
                Objects.equals(brandId, game.brandId) &&
                Objects.equals(writer, game.writer) &&
                Objects.equals(painter, game.painter) &&
                Objects.equals(type, game.type) &&
                Objects.equals(tag, game.tag) &&
                Objects.equals(story, game.story);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
//                name,
//                publishDate,
//                imgUrl,
                brandId,
                writer,
                painter,
                type,
                tag,
                story);
    }


}
