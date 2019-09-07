package com.goexp.galgame.gui.view.brand;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.scene.layout.Region;

import java.net.URL;

public class CommonInfoTabController extends DefaultController {

    private static final URL VIEW_BRAND_INFO_FXML = InfoController.class.getResource("info.fxml");
    public Region node;
    private InfoController controller;


    public CommonInfoTabController() {
        init();
    }

    private void init() {

        final var loader = new FXMLLoaderProxy<Region, InfoController>(VIEW_BRAND_INFO_FXML);
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
