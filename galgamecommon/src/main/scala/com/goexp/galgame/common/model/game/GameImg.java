package com.goexp.galgame.common.model.game;

import java.util.StringJoiner;

public class GameImg {

    public String src;
    public int index;

    @Override
    public String toString() {
        return new StringJoiner(", ", GameImg.class.getSimpleName() + "[", "]")
                .add("" + index)
                .add("'" + src + "'")
                .toString();
    }
}
