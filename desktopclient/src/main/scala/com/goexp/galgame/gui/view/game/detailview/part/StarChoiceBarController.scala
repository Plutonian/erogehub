package com.goexp.galgame.gui.view.game.detailview.part

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.change.Star
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import org.controlsfx.control.Rating

class StarChoiceBarController extends DefaultController {
  @FXML
  var starRating: Rating = _

  private var targetGame: Game = _

  private var starHandler: ChangeListener[Number] = (_, _, newV) => {
    if (newV != null) {
      logger.debug(s"New:${newV.intValue()}")

      targetGame.star = newV.intValue()
      changeStarService.restart()
    }
  }

  private val changeStarService = TaskService(new Star(targetGame))

  override protected def initialize() = {
  }

  def load(game: Game) = {
    this.targetGame = game

    starRating.ratingProperty().removeListener(starHandler)
    starRating.setRating(game.star)
    starRating.ratingProperty().addListener(starHandler)

  }

}