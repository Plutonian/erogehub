package com.goexp.galgame.common.model.game;


import java.util.Set;
import java.util.stream.Stream;

public enum GameState {
    PACKAGE("ポケ版", -3),
    SAME("SAME", -2),
    BLOCK("ブロック", -1),
    UNCHECKED("-", 0),
    HOPE("気になり", 3),
    PLAYING("進行中", 80),
    PLAYED("プレイ済み", 90);

    public final String name;
    public final int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    GameState(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static GameState from(int value) throws Exception {

        return Stream.of(GameState.values())
                .filter(type -> type.value == value)
                .findFirst().orElseThrow(()->new Exception(String.valueOf(value)));
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