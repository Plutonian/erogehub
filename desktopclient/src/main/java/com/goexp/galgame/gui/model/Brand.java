package com.goexp.galgame.gui.model;

import com.goexp.galgame.common.model.BrandType;

import java.util.List;

public class Brand {

    public int id;

    public String name;

    public String website;

    public String index;

    public boolean isMain;

    public BrandType isLike;

    public String comp;

    public boolean dead;

    public List<Brand> children;

    public String getIndex() {
        return index;
    }

    public boolean isMain() {
        return isMain;
    }

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

    public boolean isDead() {
        return dead;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", index='" + index + '\'' +
                ", isMain=" + isMain +
                ", isLike=" + isLike +
                ", comp='" + comp + '\'' +
                ", dead=" + dead +
                '}';
    }
}
