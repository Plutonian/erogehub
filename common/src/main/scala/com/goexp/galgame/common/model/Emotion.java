package com.goexp.galgame.common.model;

import java.util.Set;
import java.util.stream.Stream;

public enum Emotion {

    HATE("嫌い", -99),
    NORMAL("普通", 0),
    HOPE("気になり", 1),
    LIKE("好き", 99);

    public final String name;
    public final int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    Emotion(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static Emotion from(int value) throws Exception {

        return Stream.of(Emotion.values())
                .filter(type -> type.value == value)
                .findFirst().orElseThrow(() -> new Exception(String.valueOf(value)));
    }

    public static Emotion from(String name) {

        return Stream.of(Emotion.values())
                .filter(type -> type.name.equals(name))
                .findFirst().orElseThrow();
    }

    public static Set ignore() {
        return Set.of(Emotion.HATE);
    }


    @Override
    public String toString() {
        return this.name;
    }
}
