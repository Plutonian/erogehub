package com.goexp.galgame.gui.view.guide;

import com.goexp.common.util.string.Strings;
import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.search.sub.GuideSearchTask;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.util.Websites;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.URL;

public class SearchGuideController extends DefaultController {

    private static final URL VIEW_GUIDE_SHOW_FXML = ShowPageController.class.getResource("showpage.fxml");
    /**
     * UI Com
     */

    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);

    private String key;

    @FXML
    private TextField textSearchGameKey;
    @FXML
    private Button btnSearchGame;

    @FXML
    private BorderPane searchPanel;


    private ListView<CommonGame.Guide> guideListView = new ListView<>();

    private Service<ObservableList<CommonGame.Guide>> guideService = new TaskService<>(() -> new GuideSearchTask(key));

    protected void initialize() {
        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {
                searchPanel.setCenter(guideListView);
                guideService.restart();
            }
        });

        guideService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                guideListView.setItems(newValue);
            }
        });


        guideListView.setCellFactory(guideListView -> {

            final var guideShowLoader = new FXMLLoaderProxy<Region, ShowPageController>(VIEW_GUIDE_SHOW_FXML);

            return new ListCell<>() {
                @Override
                protected void updateItem(CommonGame.Guide guide, boolean empty) {
                    super.updateItem(guide, empty);

                    setText(null);
                    setGraphic(null);
                    if (guide != null && !empty) {
                        final var link = new Hyperlink("[" + guide.from + "] " + guide.title);
                        link.setOnAction(event -> {
                            Websites.open(guide.href);
                        });

                        if (Strings.isNotEmpty(guide.html)) {
                            final var viewlink = new Hyperlink("View");
                            viewlink.setOnAction(event -> {
                                var view = new Stage();
                                view.setTitle(guide.title);
                                view.setScene(new Scene(guideShowLoader.node));
                                view.show();

                                guideShowLoader.controller.load(guide.html);

                            });

                            HBox hBox = new HBox(link, viewlink);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        } else {

                            setGraphic(link);
                        }
                    }
                }
            };
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

}
