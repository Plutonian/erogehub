package com.goexp.galgame.data.model;

import java.util.Objects;

public class Brand {

    public int id;

    public String name;

    public String website;

    public String comp="";


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand)) return false;
        Brand brand = (Brand) o;
        return id == brand.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
//                ", index='" + index + '\'' +
//                ", isMain=" + isMain +
//                ", isLike=" + isLike +
                ", comp='" + comp + '\'' +
                '}';
    }
}
