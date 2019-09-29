package com.goexp.galgame.gui.view.game.detailview.inner;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.res.gameimg.SimpleImage;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class SimpleImgPartController extends DefaultController {

    private Game game;

    @FXML
    private ListView<Game.GameImg> listSmallSimple;

    @FXML
    private ImageView largeSimple;


//        private Service<ObservableList<Game.GameImg>> imgListService = new TaskService<>(() -> new GameImgListTask(game.id));


    protected void initialize() {


//            imgListService.valueProperty().addListener((observable, oldValue, newValue) -> {
//                if (newValue != null) {
//                    listSmallSimple.setItems(FXCollections.observableArrayList(newValue));
//
//                    if (newValue.size() > 0) {
//                        listSmallSimple.getSelectionModel().select(0);
//                    }
//                }
//            });

        listSmallSimple.setCellFactory(gameImgListView -> new ListCell<>() {
            @Override
            protected void updateItem(Game.GameImg item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(null);
                setText(null);

                if (!empty) {

                    new SimpleImage(game).onOK((img) -> {
                        setGraphic(new ImageView(img));
                        return null;
                    }).small(item.index, item.src);
                }

            }
        });

        listSmallSimple.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, simpleLargeImage) -> {

            if (simpleLargeImage != null) {

                new SimpleImage(game).onOK((img) -> {
                    largeSimple.setImage(img);
                    return null;
                }).large(simpleLargeImage.index, simpleLargeImage.src);

//                largeSimple.setImage(SimpleImage.large(game, simpleLargeImage.index, simpleLargeImage.src));
            }
        });

    }


    public void load(Game game) {

        this.game = game;
        listSmallSimple.getSelectionModel().clearSelection();
        largeSimple.setImage(null);

        listSmallSimple.setItems(FXCollections.observableArrayList(game.gameImgs));
        listSmallSimple.getSelectionModel().select(0);

        logger.debug("{}", game);

    }


    @FXML
    private void btnPrev_OnAction(ActionEvent event) {
        var index = listSmallSimple.getSelectionModel().getSelectedIndex();
        index--;
        if (index >= 0) {
            listSmallSimple.getSelectionModel().select(index);
            listSmallSimple.scrollTo(index);
        }
    }

    @FXML
    private void btnNext_OnAction(ActionEvent event) {
        var index = listSmallSimple.getSelectionModel().getSelectedIndex();
        index++;
        if (index < listSmallSimple.getItems().size()) {
            listSmallSimple.getSelectionModel().select(index);
            listSmallSimple.scrollTo(index);
        }
    }

}
