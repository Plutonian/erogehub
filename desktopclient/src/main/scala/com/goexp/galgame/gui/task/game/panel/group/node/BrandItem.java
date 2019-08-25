package com.goexp.galgame.gui.task.game.panel.group.node;

import com.goexp.galgame.gui.model.Brand;

public class BrandItem extends DefaultItem {

    public final Brand brand;

    public BrandItem(String title, int count, Brand brand) {
        super(title, count);
        this.brand = brand;
    }

}