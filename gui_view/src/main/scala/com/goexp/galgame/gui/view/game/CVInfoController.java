package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.gui.task.CVListTask;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class CVInfoController extends DefaultController {

    @FXML
    private TableView<CV> tableCV;

    @FXML
    private TableColumn<CV, String> colName;
    @FXML
    private TableColumn<CV, Integer> colStar;
    @FXML
    private TableColumn<CV, List<String>> colTag;
    @FXML
    private TableColumn<CV, LocalDate> colStart;
    @FXML
    private TableColumn<CV, LocalDate> colEnd;
    @FXML
    private TableColumn<CV, Integer> colSize;


    private final Service<ObservableList<CV>> loadCVService = new TaskService<>(CVListTask::new);


    protected void initialize() {

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStar.setCellValueFactory(new PropertyValueFactory<>("star"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        colTag.setCellValueFactory(new PropertyValueFactory<>("tag"));

        colStar.setCellFactory(col -> {
            final var image = LocalRes.HEART_16_PNG().get();
            return new TableCell<>() {
                protected void updateItem(Integer star, boolean empty) {
                    super.updateItem(star, empty);
                    this.setGraphic(null);
                    this.setText(null);


                    if (star != null && !empty) {

                        if (star > 0) {
                            var starBox = new HBox();
                            for (var i = 0; i < star; i++) {
                                starBox.getChildren().add(new ImageView(image));
                            }

                            this.setGraphic(starBox);

                        } else {
                            this.setText(star.toString());
                        }
                    }
                }
            };
        });
        colName.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);
                this.setText(null);

                if (item != null && !empty) {
                    var link = new Hyperlink(item);
                    link.setOnAction(event -> HomeController.$this().loadCVTab(item, true));
                    this.setGraphic(link);
                }
            }
        });

        colStart.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                if (item != null && !empty)
                    this.setText(String.valueOf(item.getYear()));
            }
        });

        colEnd.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                if (item != null && !empty)
                    this.setText(String.valueOf(item.getYear()));
            }
        });


        colTag.setCellFactory(col -> new TableCell<>() {
            protected void updateItem(List<String> tag, boolean empty) {
                super.updateItem(tag, empty);
                this.setGraphic(null);
                this.setText(null);

                if (tag != null && !empty) {

                    var hbox = new HBox();
                    hbox.setSpacing(5);
                    hbox.getChildren().setAll(Tags.toNodes(tag));
                    this.setGraphic(hbox);
                }
            }
        });


        loadCVService.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
//                var nodes = newValue.stream()
//                        .collect(Collectors.groupingBy(cv -> cv.star))
//                        .entrySet().stream()
//                        .sorted(Comparator.comparing(((Map.Entry entry) -> (Integer) entry.getKey())).reversed())
//                        .map(entry -> {
//                            var flow = new FlowPane();
//                            var panel = new TitledPane(entry.getKey().toString(), flow);
//                            panel.setCollapsible(false);
//                            panel.setPrefWidth(1000);
//
//                            var links = entry.getValue().stream()
//                                    .map(cv -> {
//
//                                        var link = new Hyperlink(cv.name);
////                                        link.setStyle("-fx-font-size:18px");
//                                        link.getStyleClass().add("tag");
//
//                                        return link;
//                                    })
//                                    .collect(Collectors.toUnmodifiableList());
//
//                            flow.getChildren().setAll(links);
//
//
//                            return panel;
//                        })
//                        .collect(Collectors.toUnmodifiableList());
//
//                cvFlow.getChildren().setAll(nodes);


                tableCV.setItems(newValue);
            }

        });


    }


    public void load() {

        loadCVService.restart();

    }


}
