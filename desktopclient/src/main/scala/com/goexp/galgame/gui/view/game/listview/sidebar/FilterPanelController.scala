package com.goexp.galgame.gui.view.game.listview.sidebar

import java.time.LocalDate
import java.util

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.layout.Pane
import org.controlsfx.control.ToggleSwitch

import scala.jdk.CollectionConverters._

class FilterPanelController extends FilterController[Game] {
  @FXML private var starbox: Pane = _
  @FXML private var statebox: Pane = _
  @FXML private var locationbox: Pane = _

  @FXML private var switchAll: ToggleSwitch = _

  override protected def initialize() = reset()

  def reset() = {

    def resetState() = {
      val nodes = GameState.values.to(LazyList)
        .sortBy {
          _.value
        }.reverse
        .map(gameType => {
          val btn = new CheckBox(gameType.name)
          btn.setUserData(gameType)
          if (gameType.value > GameState.BLOCK.value)
            btn.setSelected(true)
          btn
        })
        .asJava

      statebox.getChildren.setAll(nodes)
    }

    resetState()

    def resetStar() = {
      val nodes =

        Range.inclusive(0, 5).to(LazyList).reverse
          .map(star => {
            val btn = new CheckBox(star.toString)
            btn.setUserData(star)
            btn.setSelected(true)
            btn
          }).asJava


      starbox.getChildren.setAll(nodes)
    }

    resetStar()

    def resetLocation() = {
      val nodes = List(GameLocation.REMOTE, GameLocation.NETDISK, GameLocation.LOCAL).to(LazyList)
        .map(gameLoc => {
          val btn = new CheckBox(gameLoc.name)
          btn.setUserData(gameLoc)
          btn.setSelected(true)
          btn
        })
        .asJava

      locationbox.getChildren.setAll(nodes)
    }

    resetLocation()


    setP()
  }

  @FXML private def SetFilter_OnAction(event: ActionEvent) = {
    setP()
    onSetProperty.set(true)
    onSetProperty.set(false)
  }

  private def setP() = {
    val stars = starbox.getChildren.asScala.to(LazyList)
      .filter(_.asInstanceOf[CheckBox].isSelected)
      .map(_.asInstanceOf[CheckBox].getText.toInt)

    val states = statebox.getChildren.asScala.to(LazyList)
      .filter(_.asInstanceOf[CheckBox].isSelected)
      .map(_.getUserData.asInstanceOf[GameState])

    val location = locationbox.getChildren.asScala.to(LazyList)
      .filter(_.asInstanceOf[CheckBox].isSelected)
      .map(_.getUserData.asInstanceOf[GameLocation])

    predicate = (game: Game) => {
      stars.contains(game.star.get()) &&
        states.contains(game.state.get) &&
        location.contains(game.location.get)
    }

    if (switchAll.isSelected)
      predicate = predicate.and { game: Game =>
        Option(game.publishDate).exists {
          _.isBefore(LocalDate.now())
        }
      }
  }

  override def init(filteredGames: util.List[Game]) = {
  }
}