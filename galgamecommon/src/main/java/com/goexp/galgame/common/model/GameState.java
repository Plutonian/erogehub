package com.goexp.galgame.common.model;


import java.util.stream.Stream;

public enum GameState {
    PACKAGE("ポケ版", -3),
    SAME("SAME", -2),
    BLOCK("ブロック", -1),
    UNCHECKED("...", 0),
    READYTOVIEW("あとで見る", 2),
    HOPE("気になり", 3),
    PLAYING("進行中", 80),
    PLAYED("プレイ済み", 90);

    private String name;
    private int value;

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
                .filter(type -> {
                    return type.name.equals(name);
                })
                .findFirst().orElseThrow();
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.name;
    }


}