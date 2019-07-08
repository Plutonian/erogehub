package com.goexp.galgame.gui.view.common.jump;

import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.util.Websites;
import com.goexp.galgame.gui.view.MainController;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;


public class JumpLinkController extends DefaultController {

    private Game game = new Game();

    @FXML
    private SearchController searchLinkController;

    @FXML
    private MenuItem linkGetchu;

    @FXML
    private MenuItem linkGuide;


    protected void initialize() {

        linkGetchu.setOnAction((e) -> {
            Websites.open(GetchuURL.Game.byId(game.id));
        });

        linkGuide.setOnAction((e) -> {

            HomeController.$this.loadGuide(game.name);
//            Websites.open(GetchuURL.Game.byId(game.name));
        });
    }

    public void load(Game game) {
        this.game = game;

        searchLinkController.load(game.name.split("[\\s～\\-]")[0]);

    }


}
