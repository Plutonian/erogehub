package com.goexp.galgame.gui.model;

import com.goexp.galgame.common.model.CommonBrand;

import java.util.StringJoiner;

public class Brand extends CommonBrand {

    @Override
    public String toString() {
        return new StringJoiner(", ", Brand.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("website='" + website + "'")
                .add("comp='" + comp + "'")
                .add("isLike=" + isLike)
                .toString();
    }

    //    @Override
//    public String toString() {
//        return super.toString() + "Brand{" +
//                "isLike=" + isLike +
//                "} ";
//    }
}
