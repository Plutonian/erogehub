package com.goexp.galgame.gui.view.common.control

import javafx.scene.control.SkinBase
import scalafx.Includes._
import scalafx.scene.control.Label
import scalafx.scene.layout.FlowPane

import scala.jdk.CollectionConverters._

class TagPanelSkin(control: TagPanel) extends SkinBase[TagPanel](control) {
  private lazy val container = new FlowPane()

  init()

  private def init() = {
    control.setMouseTransparent(true)

    getChildren.setAll(container)
    reCreate()

    registerChangeListener(control.tagsProperty, {
      _ => reCreate()
    })
  }


  def reCreate(): Unit = {

    if (!control.tags().isEmpty) {
      val nodes = control.tags().asScala.to(LazyList)
        .filter {
          _.nonEmpty
        }
        .map {
          new Label(_) {
            styleClass.add("tag")
            styleClass.add("tagsmall")
          }
        }

      container.children = nodes

    }
    else
      container.getChildren.clear()
  }

}


