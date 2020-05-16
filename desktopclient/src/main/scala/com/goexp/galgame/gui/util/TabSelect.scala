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
  type DataLoader = () => Unit

  private var notFoundAction: NotFindAction = _
  private var loadData: DataLoader = _

  def whenNotFound(notFindAction: => Tab): TabSelect = {
    this.notFoundAction = () => notFindAction
    this
  }

  def whenNotFound(loadData: => Unit, notFindAction: => Tab): TabSelect = {
    this.notFoundAction = () => notFindAction
    this.loadData = () => loadData
    this
  }

  def select(title: String): Unit =
    root.getTabs.asScala.to(LazyList)
      .find(_.getText == title) match {
      case Some(tab) =>
        root.getSelectionModel.select(tab)
      case None =>
        MainController().insertTab(notFoundAction())
        if (loadData != null)
          loadData()
    }

}