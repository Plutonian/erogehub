package com.goexp.galgame.gui.view.cv;

import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.game.PersonSearchTask;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

public class ListController extends DefaultController {

    private String cv;
    private boolean real;

    private TaskService<ObservableList<CommonGame.GameCharacter>> personSearchTask = new TaskService<>(() -> new PersonSearchTask.ByCV(cv, real));

    public ListView<CommonGame.GameCharacter> personList;

    @Override
    protected void initialize() {
        personList.setCellFactory(gameCharacterListView -> {

            var loader = new FXMLLoaderProxy<Region, PersonCellController>("view/cv/person_cell.fxml");

            return new ListCell<>() {
                @Override
                protected void updateItem(CommonGame.GameCharacter gameCharacter, boolean empty) {
                    super.updateItem(gameCharacter, empty);
                    setText(null);
                    setGraphic(null);

                    if (gameCharacter != null && !empty) {
                        var controller = loader.controller;
                        controller.gameChar = gameCharacter;
                        controller.init();
                        setGraphic(loader.node);
                    }
                }
            };
        });

        personSearchTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                personList.setItems(newValue);
        });
    }


    public void load(String cv, boolean real) {
        this.cv = cv;
        this.real = real;

        personSearchTask.restart();

    }
}
