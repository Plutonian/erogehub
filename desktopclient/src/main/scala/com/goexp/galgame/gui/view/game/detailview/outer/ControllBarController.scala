package com.goexp.galgame.gui.view.game.detailview.outer

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.view.common.jump.JumpLinkController
import com.goexp.galgame.gui.view.game.part.{LocationChangeController, StateChangeController}
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import scalafx.Includes._

class ControllBarController extends DefaultController {
  @FXML private var webjumpController: JumpLinkController = _
  @FXML private var changeStateController: StateChangeController = _
  @FXML private var changeLocationController: LocationChangeController = _


  def load(game: Game) = {
    loadWithoutImage(game)
  }

  private def loadWithoutImage(game: Game) = {
    changeStateController.load(game)

    changeLocationController.location <==> game.location
    changeLocationController.targetGame = game

    webjumpController.load(game)
  }
}