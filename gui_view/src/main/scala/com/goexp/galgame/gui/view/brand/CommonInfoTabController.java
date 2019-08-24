package com.goexp.galgame.gui.view.brand;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import javafx.scene.layout.Region;

public class CommonInfoTabController extends DefaultController {

    public Region node;
    private InfoController controller;


    public CommonInfoTabController() {
        init();
    }

    private void init() {

        final var loader = new FXMLLoaderProxy<Region, InfoController>("view/brand/info.fxml");
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
