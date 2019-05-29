package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.game.GameSearchTask;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.util.TabSelect;
import com.goexp.galgame.gui.util.res.Images;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.brand.CommonInfoTabController;
import com.goexp.galgame.gui.view.brand.MainPanelController;
import com.goexp.galgame.gui.view.game.detailview.NavViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends DefaultController {


    private static final String GAME_DETAIL_NAV_PAGE_FXML = "view/game_explorer/detail/out_page.fxml";
    private static final String SEARCH_TYPE_FXML = "view/search/type.fxml";
    private static final String BRAND_PANEL_FXML = "view/brand/panel.fxml";
    private static final String SEARCH_FXML = "view/search/search.fxml";
    private static final String CVINFO_FXML = "view/search/cvinfo.fxml";


    public static HomeController $this;
    public TabPane mainTabPanel;
    @FXML
    private DateController dateController;
    @FXML
    private Accordion menuPanel;

    @FXML
    private Region date;

    @FXML
    private VBox gameStateLinkPanel;

    @FXML
    private VBox gameStateLikeLinkPanel;

    @FXML
    private VBox gameStarlinkPanel;

    @FXML
    private Hyperlink linkDate;

    @FXML
    private Hyperlink linkCV;

    @FXML
    private Hyperlink linkSearch;


    @FXML
    private Hyperlink linkTags;


    public void insertTab(Tab tab) {
        insertTab(tab, true);
    }

    public void insertTab(Tab tab, boolean select) {
        var index = mainTabPanel.getSelectionModel().getSelectedIndex();
        mainTabPanel.getTabs().add(index + 1, tab);

        if (select)
            mainTabPanel.getSelectionModel().select(tab);
    }


    protected void initialize() {

        $this = this;


        dateController.onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {
                final var start = dateController.from;
                final var end = dateController.to;

                final var text = dateController.title;

                TabSelect.from().ifNotFind(() -> {

                    var conn = new CommonTabController(() -> new GameSearchTask.ByDateRange(start, end));

                    var tab = new Tab(text, conn.node);
                    tab.setGraphic(new ImageView(LocalRes.DATE_16_PNG.get()));

                    conn.load();

                    return tab;
                }).select(text);

            }
        });

        dateController.onYearLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {

                final var from = dateController.from;
                final var to = dateController.to;
                final var text = dateController.title;

                TabSelect.from().ifNotFind(() -> {

                    var conn = new CommonTabController(() -> new GameSearchTask.ByDateRange(from, to));

                    var tab = new Tab(text, conn.node);
                    tab.setGraphic(new ImageView(LocalRes.DATE_16_PNG.get()));

                    conn.load();

                    return tab;
                }).select(text);

            }
        });
        date.setVisible(false);

        menuPanel.setExpandedPane(menuPanel.getPanes().get(0));

        initBlockList();

        var links = gameType2Link(List.of(
                GameState.READYTOVIEW
                , GameState.HOPE
                , GameState.PLAYING
        ));
        gameStateLikeLinkPanel.getChildren().setAll(links);

        linkDate.setGraphic(new ImageView(Images.Local.getLocal("/img/date.png")));
        linkCV.setGraphic(new ImageView(Images.Local.getLocal("/img/cv.png")));
        linkSearch.setGraphic(new ImageView(Images.Local.getLocal("/img/search.png")));
        linkTags.setGraphic(new ImageView(Images.Local.getLocal("/img/tag.png")));


    }

    private void initBlockList() {
        var links = gameType2Link(List.of(
//                GameState.PACKAGE
//                , GameState.SAME
//                , GameState.BLOCK
//                ,
                GameState.PLAYED
        ));

        gameStateLinkPanel.getChildren().setAll(links);
    }

    private List<Hyperlink> gameType2Link(GameState[] gameState) {
        return gameType2Link(List.of(gameState));
    }

    private List<Hyperlink> gameType2Link(List<GameState> gameState) {
        return gameState.stream()
                .map(type -> {
                    var link = new Hyperlink();
                    link.setText(type.name);
                    link.setUserData(type);
                    link.setOnAction(event -> {
                        var conn = new CommonTabController(() -> new GameSearchTask.ByState(type));

                        conn.controller.tableViewController.tableColStar.setVisible(false);
                        conn.controller.tableViewController.tableColState.setVisible(false);

                        final var text = type.name;

                        TabSelect.from().ifNotFind(() -> {

                            var tab = new Tab(text, conn.node);
                            conn.load();

                            return tab;
                        })
                                .select(text);
                    });

                    return link;
                })
                .collect(Collectors.toUnmodifiableList());

    }


    public void viewBrand(Brand brand) {

        final var text = brand.name;

        TabSelect.from().ifNotFind(() -> {
            var conn = new CommonInfoTabController();

            var tab = new Tab(text, conn.node);
            tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG.get()));

            conn.load(brand);

            return tab;
        }).select(text);

    }

    public void loadPainterTab(String painter) {

        TabSelect.from().ifNotFind(() -> {

            var conn = new CommonTabController(() -> new GameSearchTask.ByPainter(painter));

            var tab = new Tab(painter, conn.node);
//                    tab.setGraphic(new ImageView(LocalRes.CV_16_PNG.get()));

            conn.load();
            return tab;
        }).select(painter);

    }

    public void loadCVTab(String cv, boolean real) {

        TabSelect.from().ifNotFind(() -> {

            var conn = new CommonTabController(() -> new GameSearchTask.ByCV(cv, real));

            var tab = new Tab(cv, conn.node);
            tab.setGraphic(new ImageView(LocalRes.CV_16_PNG.get()));

            conn.load();

            return tab;
        }).select(cv);

    }

    public void loadDetail(Game game) {
        TabSelect.from().ifNotFind(() -> {

            var loader = new FXMLLoaderProxy<Region, NavViewController>(GAME_DETAIL_NAV_PAGE_FXML);

            var tab = new Tab(game.name, loader.node);
            tab.setGraphic(new ImageView(LocalRes.GAME_16_PNG.get()));

            loader.controller.load(game);

            return tab;
        }).select(game.name);
    }


    @FXML
    private void linkSearch_OnAction(ActionEvent actionEvent) throws IOException {
        TabSelect.from().ifNotFind(() -> {

            var loader = new FXMLLoaderProxy<Region, SearchController>(SEARCH_FXML);

            var tab = new Tab("Search", loader.node);
            loader.controller.load();

            return tab;
        }).select("Search");
    }

    @FXML
    private void linkSearch_OnDragOver(DragEvent e) {

        var board = e.getDragboard();

        var files = board.getFiles();
        if (files.size() == 1) {
            e.acceptTransferModes(TransferMode.LINK);
        }
    }

    @FXML
    private void linkSearch_OnDragDropped(DragEvent e) {
        var board = e.getDragboard();
        var files = board.getFiles();

        if (files.size() > 0) {
            var f = files.get(0);
            var title = f.getName().replaceFirst("\\.[^\\.]+", "");

            TabSelect.from().ifNotFind(() -> {

                var loader = new FXMLLoaderProxy<Region, SearchController>(SEARCH_FXML);

                var tab = new Tab("Search", loader.node);

                loader.controller.load(title);

                return tab;
            }).select("Search");
        }
    }

    @FXML
    private void linkTags_OnAction(ActionEvent actionEvent) throws IOException {

        TabSelect.from().ifNotFind(() -> {
            var loader = new FXMLLoaderProxy<Region, TagExplorerController>(SEARCH_TYPE_FXML);

            var tab = new Tab("Tags", loader.node);
            loader.controller.load();

            return tab;
        }).select("Tags");
    }


    @FXML
    private void linkDate_OnAction(ActionEvent actionEvent) throws IOException {
        switchVisiable(date);

        if (date.isVisible())
            dateController.load();

    }


    @FXML
    private void linkBrand_OnAction(ActionEvent actionEvent) throws IOException {

        TabSelect.from().ifNotFind(() -> {
            final var loader = new FXMLLoaderProxy<Region, MainPanelController>(BRAND_PANEL_FXML);

            var tab = new Tab("Brand", loader.node);
            loader.controller.load();

            return tab;
        }).select("Brand");

    }

    @FXML
    private void linkGuide_OnAction(ActionEvent actionEvent) throws IOException {

        TabSelect.from().ifNotFind(() -> {
            final var loader = new FXMLLoaderProxy<Region, com.goexp.galgame.gui.view.guide.SearchController>("view/guide/panel.fxml");

            var tab = new Tab("攻略", loader.node);
            loader.controller.load();

            return tab;
        }).select("攻略");

    }

    @FXML
    private void linkCV_OnAction(ActionEvent actionEvent) {

        TabSelect.from().ifNotFind(() -> {

            final var loader = new FXMLLoaderProxy<Region, CVInfoController>(CVINFO_FXML);

            var tab = new Tab("CV", loader.node);
            tab.setGraphic(new ImageView(LocalRes.CV_16_PNG.get()));

            loader.controller.load();

            return tab;
        }).select("CV");
    }

    @FXML
    private void pass_OnAction(ActionEvent actionEvent) {

        final var title = "差";

        TabSelect.from().ifNotFind(() -> {


            var conn = new CommonTabController(() -> new GameSearchTask.ByStarRange(1, 2));

            conn.controller.tableViewController.tableColState.setVisible(false);

            var tab = new Tab(title, conn.node);

            conn.load(g -> g.star < 3);

            return tab;
        }).select(title);


    }


    @FXML
    private void like_OnAction(ActionEvent actionEvent) {

        final var title = "优";

        TabSelect.from().ifNotFind(() -> {


            var conn = new CommonTabController(() -> new GameSearchTask.ByStarRange(4, 5));

            conn.controller.tableViewController.tableColState.setVisible(false);

            var tab = new Tab(title, conn.node);

            conn.load(g -> g.star > 3);

            return tab;
        }).select(title);


    }

    @FXML
    private void normal_OnAction(ActionEvent actionEvent) {

        final var title = "良";

        TabSelect.from().ifNotFind(() -> {


            var conn = new CommonTabController(() -> new GameSearchTask.ByStarRange(3, 3));

            conn.controller.tableViewController.tableColState.setVisible(false);

            var tab = new Tab(title, conn.node);

            conn.load(g -> g.star == 3);

            return tab;
        }).select(title);


    }

    @FXML
    private void miCloseOther_OnAction(ActionEvent actionEvent) {

        var tabs = mainTabPanel.getTabs().stream()
                .filter(tab -> tab != mainTabPanel.getSelectionModel().getSelectedItem())
                .collect(Collectors.toSet());

        mainTabPanel.getTabs().removeAll(tabs);


    }

    @FXML
    private void miCloseRight_OnAction(ActionEvent actionEvent) {

        mainTabPanel.getTabs().remove(mainTabPanel.getSelectionModel().getSelectedIndex() + 1, mainTabPanel.getTabs().size());
    }


    private void switchVisiable(Node node) {
        node.setVisible(!node.isVisible());
    }


}
