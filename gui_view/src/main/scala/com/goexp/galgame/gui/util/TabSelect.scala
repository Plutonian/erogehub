package com.goexp.galgame.gui.util

import com.goexp.galgame.gui.view.MainController
import javafx.scene.control.{Tab, TabPane}

import scala.jdk.CollectionConverters._

object TabSelect {
  def apply(root: TabPane): TabSelect = new TabSelect(root)

  def apply(): TabSelect = new TabSelect(MainController().homeController.mainTabPanel)
}

class TabSelect private(val root: TabPane) {

  type NotFindAction = () => Tab

  private var notFoundAction: NotFindAction = _

  def whenNotFound(notFindAction: => Tab): TabSelect = {
    this.notFoundAction = notFindAction _
    this
  }

  def select(title: String): Unit =
    root.getTabs.asScala.to(LazyList)
      .find(_.getText == title) match {
      case Some(tab) =>
        root.getSelectionModel.select(tab)
      case None =>
        MainController().insertTab(notFoundAction())
    }

}