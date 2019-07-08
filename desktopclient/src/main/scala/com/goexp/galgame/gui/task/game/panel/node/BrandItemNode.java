package com.goexp.galgame.gui.task.game.panel.node;

import com.goexp.galgame.gui.model.Brand;

public class BrandItemNode extends DefaultItemNode {

    public Brand brand;

    public BrandItemNode(String title, int count, Brand brand) {
        super(title, count);
        this.brand = brand;
    }

}