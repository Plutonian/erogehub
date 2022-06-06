package com.goexp.galgame.common.model.game;

import java.util.Set;
import java.util.stream.Stream;

public enum PlayState {

    NOT_PLAY("-", 0),
    PLAYING("進行中", 1),
    PLAYED("プレイ済み", 2);

    public final String name;
    public final int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    PlayState(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static GameState from(int value) throws Exception {

        return Stream.of(GameState.values())
                .filter(type -> type.value == value)
                .findFirst().orElseThrow(() -> new Exception(String.valueOf(value)));
    }

    public static GameState from(String name) {

        return Stream.of(GameState.values())
                .filter(type -> type.name.equals(name))
                .findFirst().orElseThrow();
    }

    public static Set ignoreState() {
        return Set.of(GameState.BLOCK, GameState.SAME);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
