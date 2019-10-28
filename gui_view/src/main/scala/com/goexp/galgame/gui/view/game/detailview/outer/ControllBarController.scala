package com.goexp.galgame.gui.view.game.detailview.outer

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.common.jump.JumpLinkController
import com.goexp.galgame.gui.view.game.part.StateChangeController
import javafx.fxml.FXML

class ControllBarController extends DefaultController {
  @FXML private var webjumpController: JumpLinkController = _
  @FXML private var changeStateController: StateChangeController = _

  override protected def initialize() = {
  }

  def load(game: Game) = loadWithoutImage(game)

  private def loadWithoutImage(game: Game) = {
    changeStateController.load(game)
    webjumpController.load(game)
  }
}