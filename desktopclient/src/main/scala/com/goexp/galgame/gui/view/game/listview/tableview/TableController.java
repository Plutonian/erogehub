package com.goexp.galgame.gui.view.game.listview.tableview;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.game.ChangeGameTask;
import com.goexp.galgame.gui.util.*;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableController extends DefaultController {

    @FXML
    public TableView<Game> table;
    @FXML
    public TableColumn<Game, List<String>> tableColTag;
    @FXML
    public TableColumn<Game, String> tableColType;
    @FXML
    public TableColumn<Game, String> tableColBrand;
    @FXML
    public TableColumn<Game, String> tableColPainter;
    @FXML
    public TableColumn<Game, String> tableColWriter;
    @FXML
    public TableColumn<Game, GameState> tableColState;
    @FXML
    public TableColumn<Game, Integer> tableColStar;
    @FXML
    public TableColumn<Game, LocalDate> tableColDate;
    @FXML
    public TableColumn<Game, String> tableColTitle;
    @FXML
    public TableColumn<Game, String> tableColCommand;
    @FXML
    private ContextMenu menuPopup;
    private List<Game> selectedGames;


    private Service<Void> changeGameService = new TaskService<>(() -> new ChangeGameTask.MultiLike(selectedGames));


    protected void initialize() {

        var items = Stream.of(GameState.values())
//                .filter(value -> value != GameState.UNCHECKED)
                .sorted(Comparator.reverseOrder())
                .map(brandType -> {
                    var menuItem = new MenuItem();
                    menuItem.setUserData(brandType);
                    menuItem.setText(brandType.getName());
                    menuItem.setOnAction((e) -> {

                        var type = (GameState) (((MenuItem) (e.getSource())).getUserData());
                        logger.debug("<MenuItem>:{}", type.getName());


                        selectedGames = table.getSelectionModel().getSelectedItems();

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


//        table.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
//                {
//                    if (event.getTarget() instanceof TableRow) {
//                        var row = (TableRow) event.getTarget();
//
//                        if (!row.isEmpty()) {
//                            var g = (Game) row.getItem();
//                            MainController.$this.loadDetail(g);
//                        }
//                    }
//                }
//            }
//        });


        table.setRowFactory(tr -> {
            var row = new TableRow<Game>();
            row.setOnMouseClicked(null);

            row.setOnMouseClicked(event -> {

                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    if (!row.isEmpty()) {
                        var g = row.getItem();

                        HomeController.$this.loadDetail(g);
                    }
                }
            });

            return row;
        });


        tableColState.setCellValueFactory(new PropertyValueFactory<>("state"));

        tableColBrand.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().brand.name));

        tableColStar.setCellValueFactory(new PropertyValueFactory<>("star"));
        tableColPainter.setCellValueFactory(new PropertyValueFactory<>("painter"));
        tableColWriter.setCellValueFactory(new PropertyValueFactory<>("writer"));
        tableColDate.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        tableColTag.setCellValueFactory(new PropertyValueFactory<>("tag"));
        tableColTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColType.setCellValueFactory(new PropertyValueFactory<>("type"));


        tableColDate.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);
                this.setText(null);

                if (item != null && !empty) {
                    this.setText(DateUtil.formatDate(item));
                }
            }
        });

        tableColTag.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(List<String> item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);
                this.setText(null);

                if (item != null && !empty) {

                    if (item.size() > 0) {
                        var hbox = new HBox();
                        hbox.setSpacing(5);
                        hbox.getChildren().setAll(Tags.toNodes(item));
                        this.setGraphic(hbox);
                    }
                }
            }
        });


        tableColBrand.setCellFactory(col -> {
            final var loader = new FXMLLoaderProxy<Region, JumpBrandController>("view/brand/brandjump.fxml");

            return new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setGraphic(null);
                    this.setText(null);

                    if (item != null && !empty) {

                        var game = this.getTableRow().getItem();

                        if (game != null) {
                            loader.controller.load(game.brand);

                            this.setGraphic(loader.node);
                        }

                    }
                }
            };
        });

        tableColTitle.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);
                this.setText(null);

                if (item != null && !empty) {
                    var game = this.getTableRow().getItem();

                    if (game != null) {
                        var title = game.name;
                        var titleLabel = title.replaceAll("＜[^＞]*＞", "");

                        this.setText(titleLabel);
                    }


                }
            }
        });

        tableColCommand.setCellFactory(col -> {
            final var loader = new FXMLLoaderProxy<Region, JumpLinkController>("view/jump/websitejump.fxml");

            return new TableCell<>() {
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setGraphic(null);
                    this.setText(null);

                    if (item != null && !empty) {

                        var game = this.getTableRow().getItem();

                        if (game != null) {
                            Hyperlink viewLink = new Hyperlink("View");
                            viewLink.setOnAction((e) -> {

                                HomeController.$this.loadDetail(game);
                            });
                            loader.controller.load(game);

                            this.setGraphic(new HBox(viewLink, loader.node));
                        }
                    }
                }
            };
        });

        tableColState.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(GameState item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);
                this.setText(null);

                this.getTableRow().getStyleClass().remove("gray");
                if (item != null && !empty) {
                    if (item == GameState.BLOCK) {
                        this.getTableRow().getStyleClass().add("gray");
                    }
                    this.setText(item.getName());
                }
            }
        });

        tableColStar.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);
                this.setText(null);

                if (!empty) {
                    var image = LocalRes.HEART_16_PNG.get();
                    var box = new HBox();
                    for (var i = 0; i < item; i++) {
                        box.getChildren().add(new ImageView(image));
                    }

                    this.setGraphic(box);
                }
            }
        });
    }
}
