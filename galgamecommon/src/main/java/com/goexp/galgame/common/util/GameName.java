package com.goexp.galgame.common.util;

import java.util.regex.Pattern;

public class GameName {
    private static final Pattern NAME_SPLITER_REX = Pattern.compile("[〜\\s　＜「＋]");

    public static String getMainName(String str) {
        final var matcher = NAME_SPLITER_REX.matcher(str);
        final var find = matcher.find();

        return find ? str.substring(0, matcher.start()) : str;

    }

    public static String getSubName(String str) {
        final var matcher = NAME_SPLITER_REX.matcher(str);
        final var find = matcher.find();

        return find ? str.substring(matcher.start()) : "";

    }
}
