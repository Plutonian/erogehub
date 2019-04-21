package com.goexp.galgame.gui.view.brand;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import javafx.scene.layout.Region;

public class CommonBrandInfoTabController extends DefaultController {

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

    @Override
    protected void initialize() {

    }
}
