package com.goexp.galgame.gui.view.common.jump;

import com.goexp.galgame.common.website.getchu.GameList;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.util.Websites;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;


public class JumpBrandController extends DefaultController {

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

    protected void initialize() {

        linkGetchu.setOnAction((e) -> {
            Websites.open(GameList.byBrand(brand.id()));
        });
        jumpBrand.setOnAction((e) -> {
            HomeController.$this.viewBrand(brand);
        });
        linkWebsite.setOnAction((e) -> {


            Websites.open(brand.website());

        });
    }

    public void load(Brand brand) {
        this.brand = brand;

        brandLabel.setText(brand.name());

        if (brand.website() != null && brand.website().length() > 0) {
            linkWebsite.setVisible(true);
        } else {
            linkWebsite.setVisible(false);
        }

        searchLinkController.load(brand.name());

    }


}
