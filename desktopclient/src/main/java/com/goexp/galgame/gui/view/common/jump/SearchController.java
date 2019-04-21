package com.goexp.galgame.gui.view.common.jump;

import com.goexp.galgame.common.website.GGBasesURL;
import com.goexp.galgame.common.website.WikiURL;
import com.goexp.galgame.common.website._2DFURL;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.Websites;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;


public class SearchController extends DefaultController {

    private String keyword = "";

//    public MenuButton searchLink;

//    @FXML//    private MenuItem linkDlsite;

    @FXML
    private MenuItem linkGGBases;

    @FXML
    private MenuItem linkWiki;

    @FXML
    private MenuItem link2DF;

//    @FXML//    private MenuItem linkBangumi;

    protected void initialize() {

//        linkDlsite.setOnAction((e) -> {
//            Websites.open(DlSiteURL.fromTitle(keyword));
//        });
        linkGGBases.setOnAction((e) -> {
            Websites.open(GGBasesURL.fromTitle(keyword));
        });
        linkWiki.setOnAction((e) -> {
            Websites.open(WikiURL.fromTitle(keyword));
        });
        link2DF.setOnAction((e) -> {
            Websites.open(_2DFURL.fromTitle(keyword));
        });
//        linkBangumi.setOnAction((e) -> {
//            Websites.open(BangumiURL.fromTitle(keyword));
//        });
    }

    public void load(String keyword) {
        this.keyword = keyword;
    }


}
