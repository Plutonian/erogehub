package com.goexp.galgame.gui.view.search;

import com.goexp.galgame.gui.util.CommonTabController;
import com.goexp.galgame.gui.util.TabSelect;
import com.goexp.galgame.gui.view.task.GameSearchTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class SearchController {

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);

    /**
     * UI Com
     */

    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);

    private String key;

    private SearchType searchType;
    @FXML
    private TextField textSearchGameKey;
    @FXML
    private ToggleGroup searchGroup;

    public String getKey() {
        return key;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    @FXML
    private void initialize() {
        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {

                final var text = searchType.name() + ":" + key;

                TabSelect.from(MainSearchController.$this.mainTabPanel)
                        .ifNotFind(b -> {

                            var conn = new CommonTabController(new Service<>() {
                                @Override
                                protected Task createTask() {

                                    switch (searchType) {
                                        case Simple: {
                                            return new GameSearchTask.ByName(key);
                                        }
                                        case Extend: {
                                            return new GameSearchTask.ByNameEx(key);
                                        }
                                        case Full: {
                                            return new GameSearchTask.ByTag(key);
                                        }
                                    }

                                    return null;
                                }
                            });


                            var tab = new Tab(text, conn.node);
//                            tab.setGraphic(new ImageView(LocalRes.SEARCH_16_PNG.get()));

                            MainSearchController.$this.insertTab(tab);

                            conn.load();

                        })
                        .select(text);
            }
        });

    }

    public void load() {
        textSearchGameKey.setText("");
        resetEvent();
    }

    private void resetEvent() {
        onLoadProperty.set(false);
    }


    @FXML
    private void btnSearchGame_OnAction(ActionEvent actionEvent) {

        key = textSearchGameKey.getText().trim();
        searchType = SearchType.from(Integer.valueOf((String) searchGroup.getSelectedToggle().getUserData()));
        onLoadProperty.set(true);
        resetEvent();
    }

    @FXML
    private void textSearchGameKey_OnDragOver(DragEvent e) {

        var board = e.getDragboard();

        var files = board.getFiles();
        if (files.size() == 1) {
            e.acceptTransferModes(TransferMode.LINK);
        }
    }


    @FXML
    private void textSearchGameKey_OnDragDropped(DragEvent e) {
        var board = e.getDragboard();
        var files = board.getFiles();

        if (files.size() > 0) {
            var f = files.get(0);
            var title = f.getName().replaceFirst("\\.[^\\.]+", "");

            textSearchGameKey.setText(title);
        }
    }

    public enum SearchType {
        Simple(0),
        Full(1),
        Extend(2);

        private int value;

        SearchType(int value) {
            this.value = value;
        }

        public static SearchType from(int value) {
            return Arrays.stream(SearchType.values()).filter(type -> type.value == value).findAny().orElse(null);
        }
    }
}
