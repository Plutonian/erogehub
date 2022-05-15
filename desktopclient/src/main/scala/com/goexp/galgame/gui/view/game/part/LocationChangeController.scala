package com.goexp.galgame.gui.view.game.part

import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.change.Location
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import scalafx.Includes._

import scala.jdk.CollectionConverters._

class LocationChangeController extends DefaultController {
  @FXML private var choiceLocation: ChoiceBox[GameLocation] = _

  var targetGame: Game = _

  final lazy val location = new SimpleObjectProperty[GameLocation]


  final private val changeGameLocationService = TaskService(new Location(targetGame))


  override protected def initComponent() = {
    val types = Seq(GameLocation.REMOTE, GameLocation.LOCAL).asJava
    choiceLocation.setItems(FXCollections.observableArrayList(types))
  }


  override protected def dataBinding(): Unit = {
    choiceLocation.value <==> location
  }

  override protected def eventBinding(): Unit = {
    location.onChange((_, oldValue, newValue) => {
      if (oldValue != null && newValue != null) {

        logger.debug(s"<Action>Old:${oldValue},New:${newValue}")

        changeGameLocationService.restart()
      }
    })
  }

}