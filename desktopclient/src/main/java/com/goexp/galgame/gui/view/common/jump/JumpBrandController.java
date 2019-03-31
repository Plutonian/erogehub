package com.goexp.galgame.gui.view.common.jump;

import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.HGameApp;
import com.goexp.galgame.gui.view.search.MainSearchController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;


public class JumpBrandController {

    private Brand brand = new Brand();


    @FXML
    private SearchController searchLinkController;

    @FXML
    private MenuButton brandLabel;

    @FXML
    private MenuItem jumpBrand;

    @FXML
    private MenuItem linkWebsite;

    @FXML
    private MenuItem linkGetchu;


    @FXML
    private void initialize() {

        linkGetchu.setOnAction((e) -> {
            HGameApp.app.getHostServices().showDocument(GetchuURL.getListByBrand(brand.id));
        });
        jumpBrand.setOnAction((e) -> {
            MainSearchController.$this.viewBrand(brand);
        });
        linkWebsite.setOnAction((e) -> {


            HGameApp.app.getHostServices().showDocument(brand.website);

        });
    }

    public void load(Brand brand) {
        this.brand = brand;

        brandLabel.setText(brand.name);

        if (brand.website != null && brand.website.length() > 0) {
            linkWebsite.setVisible(true);
        } else {
            linkWebsite.setVisible(false);
        }

        searchLinkController.load(brand.name);

    }


}
