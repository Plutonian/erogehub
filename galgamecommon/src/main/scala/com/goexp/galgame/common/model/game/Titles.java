package com.goexp.galgame.common.model.game;

import java.util.StringJoiner;

public class Titles {
    public final String mainTitle;
    public final String subTitle;

    public Titles(String mainTitle, String subTitle) {
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Titles.class.getSimpleName() + "[", "]")
                .add("mainTitle='" + mainTitle + "'")
                .add("subTitle='" + subTitle + "'")
                .toString();
    }
}
