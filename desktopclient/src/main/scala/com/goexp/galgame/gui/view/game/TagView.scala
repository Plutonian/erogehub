package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.TagType
import com.goexp.galgame.gui.task.TagListTask
import com.goexp.galgame.gui.task.game.search.ByTag
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{Datas, EventProcessor, TabManager}
import com.goexp.galgame.gui.view.common.control.DataTab
import com.goexp.ui.javafx.TaskService
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.{Insets, Orientation, VPos}
import javafx.scene.control.{Hyperlink, ScrollPane, TitledPane}
import javafx.scene.image.ImageView
import javafx.scene.layout.FlowPane

import scala.jdk.CollectionConverters._

class TagView extends ScrollPane with Datas {

  object Data {

    val typeService = TaskService(new TagListTask())

    typeService.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {
        val contents = newValue.to(LazyList)
          .map((tagType: TagType) => {
            val flowPanel = new FlowPane

            val tags = tagType.tags.to(LazyList)
              .map((tag: String) => {
                new Hyperlink(tag) {
                  getStyleClass.add("tag")
                }

              })
              .asJava

            flowPanel.getChildren.setAll(tags)

            new TitledPane(tagType.`type`, flowPanel) {
              setPrefWidth(250)
              setCollapsible(false)
            }
          })
          .asJava

        main.getChildren.setAll(contents)
      }
    })

    def load() = {
      typeService.restart()
    }
  }

  object Resource {

  }

  object Events extends EventProcessor {
    val eventFilter: EventHandler[ActionEvent] = e =>
      e.getTarget match {
        case link: Hyperlink =>
          val targetTag = link.getText

          TabManager().open(targetTag,
            new DataTab(CommonDataViewPanel(new ByTag(targetTag))) {
              setText(targetTag)
              setGraphic(new ImageView(LocalRes.TAG_16_PNG))
            }
          )
        case _ =>
      }

    override def dispose(): Unit = {
      main.removeEventFilter(ActionEvent.ACTION, eventFilter)
    }
  }

  val main = new FlowPane() {
    setHgap(5)
    setVgap(5)
    setOrientation(Orientation.VERTICAL)
    setRowValignment(VPos.TOP)
    setPadding(new Insets(3, 3, 3, 3))

    addEventFilter(ActionEvent.ACTION, Events.eventFilter)
  }

  setFitToHeight(true)
  this.setContent(main)

  override def load(): Unit = {
    Data.load()
  }
}
