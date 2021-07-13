package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.TagType
import com.goexp.galgame.gui.task.TagListTask
import com.goexp.galgame.gui.task.game.search.ByTag
import com.goexp.galgame.gui.util.TabManager
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Tab, TitledPane}
import javafx.scene.image.ImageView
import javafx.scene.layout.FlowPane

import scala.jdk.CollectionConverters._


class TagController extends DefaultController {
  final val onLoadProperty = new SimpleBooleanProperty(false)
  var tag: String = _
  @FXML private var tabType: FlowPane = _
  final private val typeService = TaskService(new TagListTask())

  override protected def initialize() = {
    tabType.addEventFilter(ActionEvent.ACTION, { e: ActionEvent =>
      e.getTarget match {
        case link: Hyperlink =>
          tag = link.getText
          onLoadProperty.set(true)
          onLoadProperty.set(false)
        case _ =>
      }

    })
    typeService.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {
        val contents = newValue.to(LazyList)
          .map((tagType: TagType) => {
            val flowPanel = new FlowPane
            val titlePanel = new TitledPane(tagType.`type`, flowPanel)
            titlePanel.setPrefWidth(250)
            titlePanel.setCollapsible(false)

            val tags = tagType.tags.to(LazyList)
              .map((tag: String) => {
                val link = new Hyperlink(tag)
                link.getStyleClass.add("tag")
                link

              })
              .asJava

            flowPanel.getChildren.setAll(tags)

            titlePanel
          })
          .asJava

        tabType.getChildren.setAll(contents)
      }
    })
    onLoadProperty.addListener((_, _, newValue) => {
      if (newValue != null && newValue) {
        val targetTag = tag


        val conn = CommonTabController(new ByTag(targetTag))

        TabManager().open(targetTag, {
          new Tab(targetTag, conn.node) {
            setGraphic(new ImageView(LocalRes.TAG_16_PNG))
          }
        }) {
          conn.load()
        }

      }
    })
  }

  def load() = {
    onLoadProperty.set(false)
    typeService.restart()
  }
}