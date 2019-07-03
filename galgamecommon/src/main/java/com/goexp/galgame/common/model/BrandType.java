package com.goexp.galgame.common.model;


import java.util.stream.Stream;

public enum BrandType {
    ALL("ALL", -99),
    BLOCK("ブロック", -2),
    UNCHECKED("...", 0),
    NORMAL("普通", 4),
    HOPE("欲しい", 80),
    CHECKING("checking", 85),
    LIKE("好き", 99),
    MARK("mark", 100);


    public final String name;
    public final int value;

    BrandType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static BrandType from(int value) {

        return Stream.of(BrandType.values())
                .filter(type -> {
                    return type.value == value;
                })
                .findFirst().orElseThrow();
    }

    public static BrandType from(String name) {

        return Stream.of(BrandType.values())
                .filter(type -> {
                    return type.name.equals(name);
                })
                .findFirst().orElseThrow();
    }

    @Override
    public String toString() {
        return this.name;
    }

}