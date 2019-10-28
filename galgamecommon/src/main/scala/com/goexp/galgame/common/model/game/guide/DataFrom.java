package com.goexp.galgame.common.model.game.guide;

import java.util.Arrays;

public enum DataFrom {
    seiya_saiga_com(1, "誠也の部屋", "http://seiya-saiga.com/game/kouryaku.html"),
    sagaoz_net(2, "愚者の館", "http://sagaoz.net/foolmaker/game.html");

    public final int value;
    public final String name;
    public final String href;


    DataFrom(int value, String name, String href) {
        this.value = value;
        this.name = name;
        this.href = href;
    }

    public static DataFrom from(int value) {
        return Arrays.stream(values()).filter(from -> from.value == value).findFirst().orElseThrow(() -> new RuntimeException("Error create DataFrom from:" + value));
    }

    @Override
    public String toString() {
        return this.name;
    }
}
