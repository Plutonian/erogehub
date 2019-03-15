package com.goexp.galgame.common.model;

import java.util.List;
import java.util.Objects;

public class TagType {
    public String type;
    public int order;

    public List<String> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagType)) return false;
        TagType tagType = (TagType) o;
        return type.equals(tagType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "TagType{" +
                "type='" + type + '\'' +
                ", order=" + order +
                '}';
    }
}