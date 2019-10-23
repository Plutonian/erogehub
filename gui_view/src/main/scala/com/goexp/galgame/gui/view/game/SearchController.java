package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.gui.task.game.search.ByName;
import com.goexp.galgame.gui.task.game.search.ByNameEx;
import com.goexp.galgame.gui.task.game.search.ByTag;
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

public class SearchController extends DefaultController {

    public final BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);

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
                            return new ByName(key);
                        }
                        case Extend: {
                            return new ByNameEx(key);
                        }
                        case Full: {
                            return new ByTag(key);
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
        searchType = SearchType.from(Integer.parseInt((String) searchGroup.getSelectedToggle().getUserData()));
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
            var title = f.getName().replaceFirst("\\.[^.]+", "");

            textSearchGameKey.setText(title);
        }
    }

}
