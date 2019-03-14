package com.goexp.galgame.gui.model;

import com.goexp.galgame.common.model.BrandType;

import java.util.List;

public class Brand {

    public int id;

    public String name;

    public String website;

    public BrandType isLike;

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

    public BrandType getIsLike() {
        return isLike;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", isLike=" + isLike +
                ", comp='" + comp + '\'' +
                '}';
    }
}
