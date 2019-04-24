package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.common.model.TagType;
import com.goexp.galgame.gui.task.TagListTask;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.GameSearchTask;
import com.goexp.galgame.gui.util.*;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.util.stream.Collectors;

public class TagExplorerController extends DefaultController {


    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);
    public String tag;
    @FXML
    private FlowPane tabType;
    private Service<ObservableList<TagType>> typeService = new TaskService<>(() -> new TagListTask());


    /**
     * Event
     */


    protected void initialize() {

        tabType.addEventFilter(ActionEvent.ACTION, event -> {

            if (event.getTarget() instanceof Hyperlink) {

                var link = (Hyperlink) event.getTarget();

                tag = link.getText();
                onLoadProperty.set(true);
                onLoadProperty.set(false);
            }
        });


        typeService.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                var contents = newValue.stream()
                        .map(tagType -> {

                            var flowPanel = new FlowPane();
                            var titlePanel = new TitledPane(tagType.type, flowPanel);
                            titlePanel.setPrefWidth(250);
                            titlePanel.setCollapsible(false);


                            var tags = tagType.tags.stream()
                                    .map(tag -> {
                                        var link = new Hyperlink(tag);
//                                        link.setStyle("-fx-font-size:18px");
                                        link.getStyleClass().add("tag");
//                                        link.setOnAction(e -> {
//                                            tag = link.getText();
//
//                                            gameSearchByTagService.restart();
//
//                                            tagsPanel.setText("Tag:" + tag);
//
//                                            showSearch();
//                                        });

                                        return link;
                                    }).collect(Collectors.toList());

                            flowPanel.getChildren().setAll(tags);


                            return titlePanel;
                        })
                        .collect(Collectors.toList());

                tabType.getChildren().setAll(contents);

            }

        });

        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {

                final var targetTag = tag;

                TabSelect.from()
                        .ifNotFind(() -> {
                            var conn = new CommonTabController(() -> new GameSearchTask.ByTag(targetTag));

                            var tab = new Tab(targetTag, conn.node);
                            tab.setGraphic(new ImageView(LocalRes.TAG_16_PNG.get()));
                            conn.load();

                            return tab;

                        })
                        .select(targetTag);
            }
        });

    }

    public void load() {
        onLoadProperty.set(false);
        typeService.restart();
    }


}
