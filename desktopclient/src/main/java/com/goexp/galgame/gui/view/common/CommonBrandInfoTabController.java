package com.goexp.galgame.gui.view.common;

import com.goexp.galgame.gui.view.search.frombrand.brand.BrandInfoController;
import com.goexp.galgame.gui.model.Brand;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import java.io.IOException;

public class CommonBrandInfoTabController {

    public Region node;
    private BrandInfoController controller;


    public CommonBrandInfoTabController() {
        init();
    }

    private void init() {

        var dataView = new BrandInfoView();
        dataView.invoke();
        node = dataView.node;

        controller = dataView.controller;
        controller.dataViewController.tableViewController.tableColBrand.setVisible(false);
    }

    public void load(Brand brand) {
        controller.load(brand);
    }

    private class BrandInfoView {
        public BrandInfoController controller;

        public Region node;

        public void invoke() {
            var loader = new FXMLLoader(getClass().getClassLoader().getResource("view/brand/brand_info.fxml"));
            try {
                node = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller = loader.getController();
        }
    }


}
