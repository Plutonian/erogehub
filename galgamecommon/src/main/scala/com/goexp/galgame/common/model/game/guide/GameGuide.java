package com.goexp.galgame.common.model.game.guide;

import java.util.Objects;
import java.util.StringJoiner;

public class GameGuide {
    public String id;
    public String title;
    public String href;
    public String html;
    public DataFrom from;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameGuide)) return false;
        GameGuide gameGuide = (GameGuide) o;
        return id.equals(gameGuide.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GameGuide.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("title='" + title + "'")
                .add("href='" + href + "'")
                .add("from=" + from)
                .toString();
    }

}
