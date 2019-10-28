package com.goexp.galgame.gui.view.game.listview.tableview;

import com.goexp.common.util.date.DateUtil;
import com.goexp.galgame.common.model.game.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.change.MultiLike;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableController extends DefaultController {

    @FXML
    public TableView<Game> table;

    /**
     * Columns
     */

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
    private ContextMenu menuPopup;
    private List<Game> selectedGames;


    private final Service<Void> changeGameService = new TaskService<>(() -> new MultiLike(selectedGames));


    protected void initialize() {

        var items = Stream.of(GameState.values())
                .sorted(Comparator.reverseOrder())
                .map(brandType -> {
                    var menuItem = new MenuItem();
                    menuItem.setUserData(brandType);
                    menuItem.setText(brandType.name);
                    menuItem.setOnAction((e) -> {

                        var type = (GameState) (((MenuItem) (e.getSource())).getUserData());
                        logger.debug("<MenuItem>:{}", type.name);


                        selectedGames = table.getSelectionModel().getSelectedItems();

                        selectedGames.forEach(game -> game.state.set(type));

                        changeGameService.restart();
                    });

                    return menuItem;
                })
                .collect(Collectors.toList());

        menuPopup.getItems().setAll(items);


        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        table.setRowFactory(tr -> {
            var row = new TableRow<Game>();
            row.setOnMouseClicked(null);

            row.setOnMouseClicked(event -> {

                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    if (!row.isEmpty()) {
                        var g = row.getItem();

                        HomeController.$this().loadDetail(g);
                    }
                }
            });

            return row;
        });


        tableColState.setCellValueFactory(new PropertyValueFactory<>("state"));

        tableColBrand.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().brand.name()));

        tableColStar.setCellValueFactory(new PropertyValueFactory<>("star"));
        tableColPainter.setCellValueFactory(new PropertyValueFactory<>("painter"));
        tableColWriter.setCellValueFactory(new PropertyValueFactory<>("writer"));
        tableColDate.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
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

                        if (game.tag.size() > 0) {
                            var hbox = new HBox();
                            hbox.setSpacing(5);
                            hbox.getChildren().setAll(Tags.toNodes(game.tag));
                            this.setGraphic(new HBox(new Label(title), hbox));
                        } else {
                            this.setText(titleLabel);
                        }
                    }
                }
            }
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
                    this.setText(item.name);
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
                    var image = LocalRes.HEART_16_PNG();
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
