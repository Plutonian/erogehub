package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.gui.task.game.GameSearchTask;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;

public class SearchController extends DefaultController {

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
    @FXML
    private Button btnSearchGame;

    @FXML
    private BorderPane searchPanel;

    protected void initialize() {
        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {

                var conn = new CommonTabController(() -> {
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
                });

                searchPanel.setCenter(conn.node);
                conn.load();

            }
        });

        btnSearchGame.disableProperty().bind(textSearchGameKey.textProperty().isEmpty());

    }

    public void load() {

        load("");
    }

    public void load(String title) {
        textSearchGameKey.setText(title);
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
            return Arrays.stream(SearchType.values()).filter(type -> type.value == value).findFirst().orElseThrow();
        }
    }
}
