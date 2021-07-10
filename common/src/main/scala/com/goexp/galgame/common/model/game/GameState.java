package com.goexp.galgame.common.model.game;


import java.util.Set;
import java.util.stream.Stream;

public enum GameState {
    PACKAGE("ポケ版", -3),
    SAME("SAME", -2),
    BLOCK("ブロック", -1),
    UNCHECKED("...", 0),
    READYTOVIEW("後で見る", 2),
    HOPE("気になり", 3),
    //    BOUGHT("入荷済み", 4),
    PLAYING("進行中", 80),
    PLAYED("プレイ済み", 90);

    public final String name;
    public final int value;

    GameState(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static GameState from(int value) {

        return Stream.of(GameState.values())
                .filter(type -> type.value == value)
                .findFirst().orElseThrow();
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