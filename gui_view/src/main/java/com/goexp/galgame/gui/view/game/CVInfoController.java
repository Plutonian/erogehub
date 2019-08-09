package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.gui.task.CVListTask;
import com.goexp.galgame.gui.task.TaskService;
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

public class CVInfoController extends DefaultController {

//    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);
    /***
     * Biz
     */

//    public String cv;
    /**
     * UI Com
     */

    public TableView<CV> tableCV;

    public TableColumn<CV, String> colName;
    public TableColumn<CV, Integer> colStar;


    @FXML
//    private FlowPane cvFlow;
//    private VBox cvFlow;
    private Service<ObservableList<CV>> loadCVService = new TaskService<>(CVListTask::new);


    /**
     * Event
     */


    protected void initialize() {

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStar.setCellValueFactory(new PropertyValueFactory<>("star"));


        colName.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);
                this.setText(null);

                if (item != null && !empty) {
                    var link = new Hyperlink(item);
                    link.setOnAction(event -> {
                        HomeController.$this.loadCVTab(item, true);
                    });
                    this.setGraphic(link);
                }
            }
        });
//
        colStar.setCellFactory(col -> {
                    final var image = LocalRes.HEART_16_PNG.get();
                    return new TableCell<>() {
                        protected void updateItem(Integer star, boolean empty) {
                            super.updateItem(star, empty);
                            this.setGraphic(null);
                            this.setText(null);


                            if (star != null && !empty) {

                                if(star>0)
                                {
                                    var starBox = new HBox();
                                    for (var i = 0; i < star; i++) {
                                        starBox.getChildren().add(new ImageView(image));
                                    }

                                    this.setGraphic(starBox);

                                }else{
                                    this.setText(star.toString());
                                }
                            }
                        }
                    };
                }

        );


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

//        onLoadProperty.addListener((observable, oldValue, newValue) -> {
//            if (newValue != null && newValue) {
//                HomeController.$this.loadCVTab(cv, true);
//            }
//        });

//        cvFlow.addEventFilter(ActionEvent.ACTION, event -> {
//
//            if (event.getTarget() instanceof Hyperlink) {
//                var link = (Hyperlink) event.getTarget();
//                cv = link.getText();
//                onLoadProperty.set(true);
//                onLoadProperty.set(false);
//            }
//        });


    }


    public void load() {

        loadCVService.restart();

    }


}