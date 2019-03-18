package com.goexp.galgame.common.model;


import java.util.stream.Stream;

public enum GameState {
    PACKAGE("PACKAGE", -3),
    SAME("SAME", -2),
    BLOCK("ブロック", -1),
    UNCHECKED("...", 0),
    READYTOVIEW("あとで見る", 2),
    HOPE("欲しい", 3),
    NEEDDOWNLOAD("ND-Down", 50),
    PLAYING("PLAYING", 80),
    PLAYED("PLAYED", 90);
//    REPLAY("REPLAY", 99);

    private String name;
    private int value;

    GameState(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static GameState from(int value) {

        return Stream.of(GameState.values())
                .filter(type -> {
                    return type.value == value;
                })
                .findFirst()
                .get();
    }

    public static GameState from(String name) {

        return Stream.of(GameState.values())
                .filter(type -> {
                    return type.name.equals(name);
                })
                .findFirst()
                .get();
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