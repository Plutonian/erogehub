package com.goexp.galgame.gui.view.guide

import com.goexp.galgame.common.model.game.guide.GameGuide
import com.goexp.galgame.gui.task.game.search.sub.GuideSearchTask
import com.goexp.galgame.gui.util.{Controller, Websites}
import com.goexp.ui.javafx.TaskService
import scalafx.beans.property.StringProperty
import scalafx.geometry.Pos
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, HBox, Priority, StackPane}

class SearchView extends BorderPane with Controller {

  lazy val searchKey = new StringProperty()

  private object DataSource {
    lazy val guideService = TaskService(new GuideSearchTask(searchKey.get()))
  }

  import DataSource._


  stylesheets.add("/view/view.css")
  styleClass.add("back")


  top = new HBox {

    spacing = 5.0
    style = "-fx-background-color: white;"
    alignment = Pos.Center

    val textSearchGameKey = new TextField() {
      promptText = "好みのゲームネイムをここで入れでください"
      hgrow = Priority.Always

      text <==> searchKey
    }


    children = Seq(
      textSearchGameKey,
      new Button {
        text = "Search"
        defaultButton = true
        mnemonicParsing = false

        disable <== textSearchGameKey.text.isEmpty

        onAction = _ => guideService.restart()

      }
    )
  }
  center = new StackPane {
    prefHeight = 150.0
    prefWidth = 200.0

    children = Seq(
      new ListView[GameGuide] {
        items <== guideService.valueProperty()

        cellFactory = _ => {

          new ListCell[GameGuide] {
            item.onChange { (_, _, guide) => {
              graphic = if (guide != null) {
                new Hyperlink() {
                  text = s"[${guide.from}] ${guide.title}"
                  onAction = (_ => Websites.open(guide.href))
                }
              } else
                null
            }
            }
          }
        }
      }
    )
  }

  override def load(): Unit = {

  }
}