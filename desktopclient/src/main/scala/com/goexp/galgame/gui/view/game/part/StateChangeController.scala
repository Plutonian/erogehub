package com.goexp.galgame.gui.view.game.part

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.change.{Block, State}
import com.goexp.galgame.gui.view.DefaultController
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.util.StringConverter

import scala.jdk.CollectionConverters._

class StateChangeController extends DefaultController {
  @FXML private var choiceState: ChoiceBox[GameState] = _

  private var targetGame: Game = _
  final private val changeGameStateService = TaskService(new State(targetGame))
  final private val blockService = TaskService(new Block(targetGame))

  private val listener: ChangeListener[GameState] = (_, _, newState) => {
    if (newState != null) {
      logger.debug(s"<Action>Value:${choiceState.getValue},New:${newState}")
      targetGame.state.set(newState)

      //reset some info
      if (newState == GameState.BLOCK) {
        targetGame.star = 0
        targetGame.location.set(GameLocation.REMOTE)

        blockService.restart()
      } else {
        changeGameStateService.restart()
      }

    }
  }

  override protected def initialize() = {

    choiceState.setConverter(new StringConverter[GameState]() {
      override def toString(gameState: GameState) = gameState.name

      override def fromString(string: String) = GameState.from(string)
    })

    val types = GameState.values.to(LazyList).sortBy(gs => gs.value).reverse.asJava
    choiceState.setItems(FXCollections.observableArrayList(types))
  }

  def load(game: Game) = {

    choiceState.valueProperty.removeListener(listener)
    choiceState.setValue(game.state.get)
    targetGame = game
    choiceState.valueProperty.addListener(listener)
  }
}