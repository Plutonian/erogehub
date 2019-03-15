package com.goexp.galgame.common.model;

public class CommonBrand {
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
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", comp='" + comp + '\'' +
                '}';
    }
}