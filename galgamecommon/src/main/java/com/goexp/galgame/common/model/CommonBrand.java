package com.goexp.galgame.common.model;

import java.util.StringJoiner;

public abstract class CommonBrand {
    public int id;
    public String name;
    public String website;
    public String comp;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getComp() {
        return comp;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CommonBrand.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("website='" + website + "'")
                .add("comp='" + comp + "'")
                .toString();
    }
}
