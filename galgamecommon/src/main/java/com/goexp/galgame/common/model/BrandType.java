package com.goexp.galgame.common.model;


import java.util.stream.Stream;

public enum BrandType {
    ALL("ALL", -99),
    PASS("ブロック", -2),
    UNCHECKED("...", 0),
    NORMAL("普通", 4),
    HOPE("欲しい", 80),
    CHECKING("checking", 85),
    LIKE("好き", 99),
    MARK("mark", 100);


    private String name;
    private int value;

    BrandType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static BrandType from(int value) {

        return Stream.of(BrandType.values())
                .filter(type -> {
                    return type.value == value;
                })
                .findFirst()
                .get();
    }

    public static BrandType from(String name) {

        return Stream.of(BrandType.values())
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