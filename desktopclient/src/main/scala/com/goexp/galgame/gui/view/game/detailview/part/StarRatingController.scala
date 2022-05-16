package com.goexp.galgame.gui.view.game.detailview.part

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.change.Star
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.SimpleDoubleProperty
import javafx.fxml.FXML
import org.controlsfx.control.Rating
import scalafx.Includes._

class StarRatingController extends DefaultController {
  @FXML var starRating: Rating = _

  private var targetGame: Game = _
  private final lazy val star = new SimpleDoubleProperty()

  private val changeStarService = TaskService(new Star(targetGame))

  override protected def initComponent() = {
    starRating.setScaleX(0.8)
    starRating.setScaleY(0.8)
  }


  override protected def eventBinding(): Unit = {

    star.onChange((_, oldValue, newValue) => {

      if (oldValue == null && newValue == null) {

      } else if (newValue != null) {
        changeStarService.restart()
      }
    })
  }


  override protected def dataBinding(): Unit = {

    starRating.ratingProperty() <==> star
  }

  def load(game: Game) = {
    this.targetGame = game

    star <==> game.star
    //    star.set(game.star.value)

  }

}