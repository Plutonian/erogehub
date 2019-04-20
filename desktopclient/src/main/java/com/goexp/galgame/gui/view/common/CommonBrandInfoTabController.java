package com.goexp.galgame.gui.view.common;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.brand.BrandInfoController;
import javafx.scene.layout.Region;

public class CommonBrandInfoTabController {

    public Region node;
    private BrandInfoController controller;


    public CommonBrandInfoTabController() {
        init();
    }

    private void init() {

        final var loader = new FXMLLoaderProxy<Region, BrandInfoController>("view/brand/brand_info.fxml");
        node = loader.node;

        controller = loader.controller;
        controller.dataViewController.tableViewController.tableColBrand.setVisible(false);
    }

    public void load(Brand brand) {
        controller.load(brand);
    }

}
