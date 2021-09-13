package com.goexp.galgame.gui.view.common.control

import com.goexp.galgame.gui.util.Tags
import javafx.scene.control.SkinBase
import scalafx.Includes._
import scalafx.scene.control.Label
import scalafx.scene.layout.FlowPane

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

    if (control.tags().size > 0)
      container.getChildren.setAll {
        Tags.toNodes(control.tags()) { str =>
          new Label(str) {
            styleClass.add("tag")
            styleClass.add("tagsmall")
          }
        }
      } else
      container.getChildren.clear()
  }

}


