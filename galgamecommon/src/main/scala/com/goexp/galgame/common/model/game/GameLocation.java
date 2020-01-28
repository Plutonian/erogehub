package com.goexp.galgame.common.model.game;

import java.util.stream.Stream;

public enum GameLocation {
    REMOTE("REMOTE", 0),
    LOCAL("LOCAL", 1);

    public final String name;
    public final int value;


    GameLocation(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static GameLocation from(int value) {

        return Stream.of(GameLocation.values())
                .filter(type -> type.value == value)
                .findFirst().orElseThrow();
    }

    public static GameLocation from(String name) {

        return Stream.of(GameLocation.values())
                .filter(type -> type.name.equals(name))
                .findFirst().orElseThrow();
    }


    @Override
    public String toString() {
        return this.name;
    }


}
