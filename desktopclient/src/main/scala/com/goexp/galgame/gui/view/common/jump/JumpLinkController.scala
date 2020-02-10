package com.goexp.galgame.gui.view.common.jump

import com.goexp.galgame.common.website.getchu.GetchuGameRemote
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Websites
import com.goexp.galgame.gui.view.MainController
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.MenuItem

class JumpLinkController extends DefaultController {
  private var game = new Game
  @FXML private var searchLinkController: SearchController = _
  @FXML private var linkGetchu: MenuItem = _
  @FXML private var linkGuide: MenuItem = _

  override protected def initialize() = {
    linkGetchu.setOnAction(_ => Websites.open(GetchuGameRemote.byId(game.id)))
    linkGuide.setOnAction(_ => MainController().loadGuide(game.name))
  }

  def load(game: Game) = {
    this.game = game
    searchLinkController.load(game.name.split("""[\s～\-]""")(0))
  }
}