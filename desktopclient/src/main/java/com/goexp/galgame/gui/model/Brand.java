package com.goexp.galgame.gui.model;

import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.common.model.CommonBrand;

public class Brand extends CommonBrand {

    public BrandType isLike;

    public BrandType getIsLike() {
        return isLike;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "isLike=" + isLike +
                "} " + super.toString();
    }
}
