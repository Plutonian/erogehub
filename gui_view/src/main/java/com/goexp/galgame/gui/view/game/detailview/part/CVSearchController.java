package com.goexp.galgame.gui.view.game.detailview.part;

import com.goexp.galgame.common.website.BangumiURL;
import com.goexp.galgame.common.website.WikiURL;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.util.Websites;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;


public class CVSearchController extends DefaultController {

    private String keyword = "";

//    public MenuButton searchLink;

    @FXML
    private MenuItem linkDlsite;

    @FXML
    private MenuItem linkWiki;

    @FXML
    private MenuItem link2DF;

    @FXML
    private MenuItem linkBangumi;

    protected void initialize() {

//        linkDlsite.setOnAction((e) -> {
//            Websites.open(DlSiteURL.fromTitle(keyword));
//        });
        linkWiki.setOnAction((e) -> {
            Websites.open(WikiURL.fromTitle(keyword));
        });
//        link2DF.setOnAction((e) -> {
//            Websites.open(_2DFURL.fromTitle(keyword));
//        });
        linkBangumi.setOnAction((e) -> {
            Websites.open(BangumiURL.fromTitle(keyword));
        });
    }

    public void load(String keyword) {
        this.keyword = keyword;
    }


}