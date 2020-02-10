package com.goexp.galgame.common.model.game.brand;


import java.util.stream.Stream;

public enum BrandState {
    ALL("ALL", -99),
    BLOCK("ブロック", -2),
    UNCHECKED("...", 0),
    NORMAL("普通", 4),
    HOPE("欲しい", 80),
    CHECKING("checking", 85),
    LIKE("好き", 99),
    MARK("mark", 100),
    EXTEND_NUKI("抜き", 101);


    public final String name;
    public final int value;

    BrandState(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static BrandState from(int value) {

        return Stream.of(BrandState.values())
                .filter(type -> type.value == value)
                .findFirst().orElseThrow();
    }

    public static BrandState from(String name) {

        return Stream.of(BrandState.values())
                .filter(type -> type.name.equals(name))
                .findFirst().orElseThrow();
    }

    @Override
    public String toString() {
        return this.name;
    }

}