package com.goexp.galgame.gui.view.common.jump;

import com.goexp.galgame.common.website.BangumiURL;
import com.goexp.galgame.common.website.WikiURL;
import com.goexp.galgame.gui.view.HGameApp;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;


public class CVSearchController {

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

    @FXML
    private void initialize() {

//        linkDlsite.setOnAction((e) -> {
//            HGameApp.app.getHostServices().showDocument(DlSiteURL.fromTitle(keyword));
//        });
        linkWiki.setOnAction((e) -> {
            HGameApp.app.getHostServices().showDocument(WikiURL.fromTitle(keyword));
        });
//        link2DF.setOnAction((e) -> {
//            HGameApp.app.getHostServices().showDocument(_2DFURL.fromTitle(keyword));
//        });
        linkBangumi.setOnAction((e) -> {
            HGameApp.app.getHostServices().showDocument(BangumiURL.fromTitle(keyword));
        });
    }

    public void load(String keyword) {
        this.keyword = keyword;
    }


}
