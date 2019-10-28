package com.goexp.galgame.common.model.game;

import java.util.StringJoiner;

public class GameCharacter {
    public String name;

    public String cv;

    public String intro;

    public String trueCV;

    public String img;
    public int index;

    @Override
    public String toString() {
        return new StringJoiner(", ", "\n" + GameCharacter.class.getSimpleName() + "[", "]")
                .add("" + index)
                .add("'" + name + "'")
                .add("cv='" + cv + "'")
//                    .add("intro='" + intro + "'")
                .add("trueCV='" + trueCV + "'")
//                    .add("img='" + img + "'")
                .toString();
    }
}
