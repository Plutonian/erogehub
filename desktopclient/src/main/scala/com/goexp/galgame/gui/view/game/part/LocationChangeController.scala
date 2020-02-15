package com.goexp.galgame.gui.view.game.part

import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.change.Location
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox

import scala.jdk.CollectionConverters._

class LocationChangeController extends DefaultController {
  @FXML private var choiceLocation: ChoiceBox[GameLocation] = _

  private var targetGame: Game = _
  final private val changeGameLocationService = TaskService(new Location(targetGame))

  private val listener: ChangeListener[GameLocation] = (_, _, newValue) => {
    if (newValue != null) {
      logger.debug(s"<Action>Value:${choiceLocation.getValue},New:${newValue}")

      targetGame.location.set(newValue)
      changeGameLocationService.restart()
    }
  }

  override protected def initialize() = {
    val types = GameLocation.values.to(LazyList).sortBy(gs => gs.value)(Ordering[Int].reverse).asJava
    choiceLocation.setItems(FXCollections.observableArrayList(types))
  }

  def load(game: Game) = {

    choiceLocation.valueProperty.removeListener(listener)
    choiceLocation.setValue(game.location.get)
    targetGame = game
    choiceLocation.valueProperty.addListener(listener)
  }
}