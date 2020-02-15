package com.goexp.galgame.gui.view.game.detailview.part

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.change.Star
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import org.controlsfx.control.Rating

class StarRatingController extends DefaultController {
  @FXML
  var starRating: Rating = _

  private var targetGame: Game = _

  private var starHandler: ChangeListener[Number] = (_, _, newV) => {
    if (newV != null) {
      logger.debug(s"New:${newV.intValue()}")

      //      targetGame.star = newV.intValue()
      //      println(targetGame.star)
      //      println(targetGame.starProperty.get())
      changeStarService.restart()
    }
  }

  private val changeStarService = TaskService(new Star(targetGame))

  override protected def initialize() = {
    starRating.setScaleX(0.8)
    starRating.setScaleY(0.8)
  }

  def load(game: Game) = {
    this.targetGame = game

    starRating.ratingProperty().removeListener(starHandler)
    //    starRating.setRating(game.star)

    starRating.ratingProperty().bindBidirectional(game.star)
    starRating.ratingProperty().addListener(starHandler)

  }

}