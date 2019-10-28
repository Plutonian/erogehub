package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.view.DefaultController
import javafx.fxml.FXML

class CellController extends DefaultController {
  @FXML private var headerController: HeaderController = _

  override protected def initialize() = {
  }

  def load(game: Game) = headerController.load(game)
}