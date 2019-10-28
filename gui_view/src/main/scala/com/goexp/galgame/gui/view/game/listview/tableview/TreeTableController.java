package com.goexp.galgame.gui.view.game.listview.tableview;

import com.goexp.common.util.date.DateUtil;
import com.goexp.galgame.common.model.game.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.change.MultiLike;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeTableController extends DefaultController {

    private static final URL VIEW_BRAND_BRANDJUMP_FXML = JumpBrandController.class.getResource("jumpbrand.fxml");
    private static final URL VIEW_JUMP_WEBSITEJUMP_FXML = JumpLinkController.class.getResource("jumplink.fxml");
    @FXML
    public TreeTableView<Game> table;
    @FXML
    public TreeTableColumn<Game, List<String>> tableColTag;
    @FXML
    public TreeTableColumn<Game, String> tableColType;
    @FXML
    public TreeTableColumn<Game, String> tableColBrand;
    @FXML
    public TreeTableColumn<Game, String> tableColPainter;
    @FXML
    public TreeTableColumn<Game, String> tableColWriter;
    @FXML
    public TreeTableColumn<Game, GameState> tableColState;
    @FXML
    public TreeTableColumn<Game, LocalDate> tableColDate;
    @FXML
    public TreeTableColumn<Game, String> tableColTitle;
    @FXML
    public TreeTableColumn<Game, String> tableColCommand;
    @FXML
    private ContextMenu menuPopup;
    private List<Game> selectedGames;


    private final Service<Void> changeGameService = new TaskService<>(() -> new MultiLike(selectedGames));

    protected void initialize() {

        var items = Stream.of(GameState.values())
//                .filter(value -> value != GameState.UNCHECKED)
                .sorted(Comparator.reverseOrder())
                .map(brandType -> {
                    var menuItem = new MenuItem();
                    menuItem.setUserData(brandType);
                    menuItem.setText(brandType.name);
                    menuItem.setOnAction((e) -> {

                        var type = (GameState) (((MenuItem) (e.getSource())).getUserData());
                        logger.debug("<MenuItem>:{}", type.name);


                        selectedGames = table.getSelectionModel().getSelectedItems()
                                .stream()
                                .map(TreeItem::getValue)
                                .collect(Collectors.toUnmodifiableList());

                        selectedGames.forEach(game -> game.state.set(type));

                        changeGameService.restart();
                    });

                    return menuItem;
                })
                .collect(Collectors.toList());

        menuPopup.getItems().setAll(items);


        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

//        table.setOnMouseClicked(event -> {
//            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
//                var g=table.getSelectionModel().getSelectedItem();
//
//                if(g!=null)
//                {
//                    MainController.$this.loadDetail(g);
//                }
//            }
//
//        });


        table.setRowFactory(tr -> {
            var row = new TreeTableRow<Game>();
            row.setOnMouseClicked(null);

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && row.getTreeItem().isLeaf()) {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        var g = row.getItem();

                        HomeController.$this().loadDetail(g);

                    }
                }
            });

            return row;
        });


        tableColState.setCellValueFactory(new TreeItemPropertyValueFactory<>("state"));

        tableColBrand.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().brand.name()));


        tableColPainter.setCellValueFactory(new TreeItemPropertyValueFactory<>("painter"));
        tableColWriter.setCellValueFactory(new TreeItemPropertyValueFactory<>("writer"));
        tableColDate.setCellValueFactory(new TreeItemPropertyValueFactory<>("publishDate"));
        tableColTag.setCellValueFactory(new TreeItemPropertyValueFactory<>("tag"));
        tableColTitle.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        tableColType.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));


        tableColDate.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                if (!empty) {
                    if (this.getTreeTableRow().getTreeItem().isLeaf()) {
                        var titleLabel = new Label(DateUtil.formatDate(item));

                        this.setGraphic(titleLabel);
                    }
                }
            }
        });

        tableColTag.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(List<String> item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                if (!empty) {

                    if (this.getTreeTableRow().getTreeItem().isLeaf()) {
                        if (item.size() > 0) {
                            var hbox = new HBox();
                            hbox.setSpacing(5);
                            hbox.getChildren().setAll(Tags.toNodes(item));
                            this.setGraphic(hbox);
                        }
                    }


                }
            }
        });


        tableColBrand.setCellFactory(col -> {

            final var loader = new FXMLLoaderProxy<Region, JumpBrandController>(VIEW_BRAND_BRANDJUMP_FXML);

            return new TreeTableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setGraphic(null);

                    if (!empty) {

                        var game = this.getTreeTableRow().getItem();

                        if (game != null) {
                            loader.controller.load(game.brand);

                            this.setGraphic(loader.node);
                        }
                    }
                }
            };
        });

//        tableColTitle.setCellFactory(col -> new TableCell<>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                this.setGraphic(null);
//
//                if (!empty) {
//                    var game = this.getTableRow().getItem();
//
//                    var title = game.name;
//                    var titleLabel = new Label(title);
//
//                    this.setGraphic(titleLabel);
//                }
//            }
//        });

        tableColCommand.setCellFactory(col -> {

            final var loader = new FXMLLoaderProxy<Region, JumpLinkController>(VIEW_JUMP_WEBSITEJUMP_FXML);

            return new TreeTableCell<>() {
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setGraphic(null);

                    if (!empty) {
                        if (this.getTreeTableRow().getTreeItem().isLeaf()) {

                            var game = this.getTreeTableRow().getItem();

                            if (game != null) {
                                Hyperlink viewLink = new Hyperlink("View");
                                viewLink.setOnAction((e) -> HomeController.$this().loadDetail(game));

                                loader.controller.load(game);

                                this.setGraphic(new HBox(viewLink, loader.node));
                            }
                        }
                    }
                }
            };
        });

        tableColState.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(GameState item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                this.getTreeTableRow().getStyleClass().remove("gray");
                if (!empty) {
                    if (this.getTreeTableRow().getTreeItem().isLeaf()) {
//                        if (item == GameState.BLOCK) {
//                            this.getTreeTableRow().getStyleClass().add("gray");
//                        }

                        this.setGraphic(new Label(item.name));
                    }
                }
            }
        });


    }
}
