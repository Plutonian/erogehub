package com.goexp.galgame.gui.view.game.search;

import java.util.Arrays;

public enum SearchType {
    Simple(0),
    Full(1),
    Extend(2);

    private final int value;

    SearchType(int value) {
        this.value = value;
    }

    public static SearchType from(int value) {
        return Arrays.stream(SearchType.values()).filter(type -> type.value == value).findFirst().orElseThrow();
    }
}
