package com.goexp.galgame.gui.view.game.part

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.change.{Block, State}
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import scalafx.Includes._

import scala.jdk.CollectionConverters._

class StateChangeController extends DefaultController {
  @FXML private var choiceState: ChoiceBox[GameState] = _

  var targetGame: Game = _

  final lazy val state = new SimpleObjectProperty[GameState]


  final private val changeGameStateService = TaskService(new State(targetGame))
  final private val blockService = TaskService(new Block(targetGame))

  override protected def initComponent() = {
    val types = GameState.values.to(LazyList).sortBy(gs => gs.value)(Ordering[Int].reverse).asJava
    choiceState.setItems(FXCollections.observableArrayList(types))
  }


  override protected def dataBinding(): Unit = {
    choiceState.value <==> state
  }


  override protected def eventBinding(): Unit = {

    state.onChange((_, oldValue, newValue) => {
      if (oldValue != null && newValue != null) {
        logger.debug(s"<Action>Value:${choiceState.getValue},New:${newValue}")

        //reset some info
        if (newValue eq GameState.BLOCK) {
          targetGame.star.set(0)
          targetGame.location.set(GameLocation.REMOTE)

          blockService.restart()
        } else {
          changeGameStateService.restart()
        }

      }
    })
  }
}