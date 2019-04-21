package com.goexp.galgame.gui.view.common.jump;

import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Websites;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;


public class JumpLinkController {

    private Game game = new Game();

    @FXML
    private SearchController searchLinkController;

    @FXML
    private MenuItem linkGetchu;


    @FXML
    private void initialize() {

        linkGetchu.setOnAction((e) -> {
            Websites.open(GetchuURL.Game.byId(game.id));
        });
    }

    public void load(Game game) {
        this.game = game;

        searchLinkController.load(game.name.split("[\\s～\\-]")[0]);

    }


}
