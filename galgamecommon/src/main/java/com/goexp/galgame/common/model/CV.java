package com.goexp.galgame.common.model;

import java.util.StringJoiner;

public class CV {
    public String name;
    public String nameStr;

    public int star;
    public String trueName;

    @Override
    public String toString() {
        return new StringJoiner(", ", CV.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("nameStr='" + nameStr + "'")
                .add("star=" + star)
                .add("trueName='" + trueName + "'")
                .toString();
    }
}