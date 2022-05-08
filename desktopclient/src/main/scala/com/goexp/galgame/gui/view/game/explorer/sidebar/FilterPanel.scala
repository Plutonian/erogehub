package com.goexp.galgame.gui.view.game.explorer.sidebar

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Controller
import javafx.event.ActionEvent
import org.controlsfx.control.ToggleSwitch
import scalafx.Includes._
import scalafx.beans.property.BooleanProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, CheckBox, Label}
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}

import java.time.LocalDate
import java.util.function.Predicate
import scala.collection.mutable

class FilterPanel extends BorderPane with Controller {

  import VO._

  stylesheets.add("/view/view.css")

  styleClass.add("copyablelabel")

  padding = Insets(5)


  center = new FlowPane() {
    hgap = 50
    children = Seq(
      new VBox() {
        spacing = 5

        children ++= Seq(
          new Label("Star"),
          new VBox {
            prefHeight = 100


            children =
              Range.inclusive(0, 5).to(LazyList).reverse
                .map(star => {
                  new CheckBox {
                    text = star.toString
                    selected = true

                    selected.onChange { (_, _, isSelected) =>
                      if (isSelected)
                        _selectedStar.add(star)
                      else
                        _selectedStar.remove(star)
                    }

                  }
                })

          },
          new Label("State"),
          new VBox {
            prefHeight = 100

            children =
              GameState.values.to(LazyList)
                .sortBy {
                  _.value
                }.reverse
                .map(gameType => {
                  new CheckBox(gameType.name) {
                    if (gameType.value > GameState.BLOCK.value)
                      selected = (true)

                    selected.onChange { (_, _, isSelected) =>
                      if (isSelected)
                        _selectedGameState.add(gameType)
                      else
                        _selectedGameState.remove(gameType)
                    }
                  }

                })

          },
          new Label("Location"),
          new VBox {
            prefHeight = 100

            children =
              List(GameLocation.REMOTE,
                //                GameLocation.NETDISK,
                GameLocation.LOCAL)
                .map(gameLoc => {
                  new CheckBox() {
                    text = gameLoc.name

                    selected = (true)

                    selected.onChange { (_, _, isSelected) =>
                      if (isSelected)
                        _selectedGameLocation.add(gameLoc)
                      else
                        _selectedGameLocation.remove(gameLoc)
                    }
                  }
                })

          },
        )

      }

    )
  }

  bottom = new HBox {
    spacing = 5
    alignment = Pos.CenterRight

    children ++= Seq(
      new ToggleSwitch("発売のみ") {
        selectedProperty <==> _switchAll
      },
      new Button() {
        text = "OK"
        onAction = SetFilter_OnAction
      }
    )
  }

  setP()

  object VO {
    lazy val _selectedStar = mutable.Set[Int](0, 1, 2, 3, 4, 5)
    lazy val _selectedGameState = mutable.Set[GameState]()

    private val states: Array[GameState] = GameState.values.filter(_.value > GameState.BLOCK.value)

    _selectedGameState.addAll(states)

    lazy val _selectedGameLocation = mutable.Set[GameLocation](
      GameLocation.REMOTE,
      //      GameLocation.NETDISK,
      GameLocation.LOCAL)

    lazy val _switchAll = new BooleanProperty
  }


  lazy val onSetProperty = new BooleanProperty()

  var predicate: Predicate[Game] = _

  private def setP() = {

    val p: Predicate[Game] = (game: Game) => {
      _selectedStar.contains(game.star.get()) &&
        _selectedGameState.contains(game.state.get) &&
        _selectedGameLocation.contains(game.location.get)
    }

    predicate =
      if (_switchAll.value) {
        p.and { (game: Game) =>
          Option(game.publishDate).exists {
            _.isBefore(LocalDate.now())
          }
        }
      } else {
        p
      }
  }

  private def SetFilter_OnAction(event: ActionEvent) = {
    setP()
    onSetProperty.set(true)
    onSetProperty.set(false)
  }


  override def load(): Unit = {
  }
}