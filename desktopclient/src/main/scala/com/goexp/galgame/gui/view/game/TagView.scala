package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.task.TagListTask
import com.goexp.galgame.gui.task.game.search.ByTag
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{Controller, TabManager}
import com.goexp.galgame.gui.view.common.control.DataTab
import com.goexp.ui.javafx.TaskService
import javafx.scene.control
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Orientation, VPos}
import scalafx.scene.control.{Hyperlink, ScrollPane, TitledPane}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.FlowPane


class TagView extends ScrollPane with Controller {

  //Init

  val main = new FlowPane() {
    hgap = 5
    vgap = 5
    padding = Insets(3, 3, 3, 3)
    orientation = Orientation.Vertical
    rowValignment = VPos.Top

    val subscription = filterEvent(ActionEvent.Action) { e: ActionEvent =>
      e.target match {
        case link: control.Hyperlink =>
          e.consume()
          val targetTag = link.text()

          TabManager().open(targetTag,
            new DataTab(CommonDataViewPanel(new ByTag(targetTag))) {
              text = (targetTag)
              graphic = (new ImageView(LocalRes.TAG_16_PNG))
            }
          )
        case _ =>
      }
    }

    def dispose() = {
      subscription.cancel()
    }

  }

  fitToHeight = (true)
  content = (main)

  override def load(): Unit = {
    DataSource.load()
  }

  override def dispose() = {
    super.dispose()
    main.dispose()
  }

  object DataSource {

    val typeService = TaskService(new TagListTask())

    typeService.valueProperty.addListener((_, _, datas) => {
      if (datas != null) {

        main.children = datas
          .map(tagType => {
            new TitledPane {
              text = tagType.`type`
              prefWidth = 250
              collapsible = false

              content = new FlowPane {
                children = tagType.tags
                  .map((tag: String) => {
                    new Hyperlink {
                      text = tag
                      styleClass.add("tag")
                    }
                  })
              }
            }
          })
      }
    })

    def load() = {
      typeService.restart()
    }
  }

  object Resource {

  }

}
