package com.goexp.galgame.gui.view.game.detailview.part

import java.util

import com.goexp.galgame.gui.model.Game
import com.goexp.ui.javafx.TaskService
import com.goexp.galgame.gui.task.game.change.Star
import com.goexp.ui.javafx.DefaultController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import javafx.scene.control.{Toggle, ToggleButton, ToggleGroup}
import javafx.scene.layout.HBox

import scala.jdk.CollectionConverters._

class StarChoiceBarController extends DefaultController {
  val onStarChangeProperty = new SimpleBooleanProperty(false)

  private var targetGame: Game = _
  private var handler: ChangeListener[Toggle] = (_, _, newV) => {
    if (newV == null) {
      targetGame.star = 0
      changeStarService.restart()
    }
    else {
      logger.debug(s"New:${newV.getUserData}")

      targetGame.star = newV.getUserData.asInstanceOf[Int]
      changeStarService.restart()
    }
    onStarChangeProperty.set(true)
    onStarChangeProperty.set(false)
  }
  private var list: util.List[ToggleButton] = _
  @FXML private var groupLikeCon: HBox = _
  private val groupLike = new ToggleGroup
  private val changeStarService = TaskService(new Star(targetGame))

  override protected def initialize() = {
    list = Range.inclusive(1, 5)
      .map(star => {
        val toggle = new ToggleButton
        toggle.setUserData(star)
        toggle.setText(star.toString)
        toggle.setToggleGroup(groupLike)
        toggle
      }).asJava

    groupLikeCon.getChildren.setAll(list)

  }

  def load(game: Game) = {
    this.targetGame = game


    groupLike.selectedToggleProperty.removeListener(handler)

    list.forEach(_.setSelected(false))
    list.asScala
      .find(btn => btn.getUserData.asInstanceOf[Int] == game.star)
      .foreach {
        _.setSelected(true)
      }

    groupLike.selectedToggleProperty.addListener(handler)
  }

}