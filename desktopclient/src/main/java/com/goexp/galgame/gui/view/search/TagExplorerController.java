package com.goexp.galgame.gui.view.search;

import com.goexp.galgame.gui.model.Tag;
import com.goexp.galgame.gui.util.CommonTabController;
import com.goexp.galgame.gui.util.LocalRes;
import com.goexp.galgame.gui.util.TabSelect;
import com.goexp.galgame.gui.view.task.GameSearchTask;
import com.goexp.galgame.gui.view.task.TagListTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class TagExplorerController {

    private final Logger logger = LoggerFactory.getLogger(TagExplorerController.class);

    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);
    public String tag;
    @FXML
    private FlowPane tabType;
    private Service<ObservableList<Tag.TagType>> typeService = new Service<>() {
        @Override
        protected Task<ObservableList<Tag.TagType>> createTask() {
            return new TagListTask();
        }
    };


    /**
     * Event
     */

    @FXML
    private void initialize() {

        tabType.addEventFilter(ActionEvent.ACTION, event -> {

            if (event.getTarget() instanceof Hyperlink) {

                var link = (Hyperlink) event.getTarget();

                tag = link.getText();
                onLoadProperty.set(true);
                onLoadProperty.set(false);
            }
        });


        ChangeListener<Throwable> exceptionHandler = (observable, oldValue, newValue) -> {
            if (newValue != null)
                newValue.printStackTrace();
        };

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
        typeService.exceptionProperty().addListener(exceptionHandler);

        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {

                final var targetTag = tag;

                TabSelect.from(MainSearchController.$this.mainTabPanel)
                        .ifNotFind(b -> {
                            var conn = new CommonTabController(new Service<>() {
                                @Override
                                protected Task createTask() {
                                    return new GameSearchTask.ByTag(targetTag);
                                }
                            });

                            var tab = new Tab(targetTag, conn.node);
                            tab.setGraphic(new ImageView(LocalRes.TAG_16_PNG.get()));

                            MainSearchController.$this.insertTab(tab);

                            conn.load();
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
