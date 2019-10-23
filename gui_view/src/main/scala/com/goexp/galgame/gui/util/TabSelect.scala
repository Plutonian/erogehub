package com.goexp.galgame.gui.util

import com.goexp.galgame.gui.view.game.HomeController
import javafx.scene.control.{Tab, TabPane}

import scala.jdk.CollectionConverters._

object TabSelect {
  def apply(root: TabPane): TabSelect = new TabSelect(root)

  def apply(): TabSelect = new TabSelect(HomeController.$this.mainTabPanel)
}

class TabSelect private(val root: TabPane) {
  private var notFind: () => Tab = _

  def ifNotFind(notFind: () => Tab): TabSelect = {
    this.notFind = notFind
    this
  }

  def select(title: String): Unit =
    root.getTabs.asScala.to(LazyList)
      .find(_.getText == title) match {
      case Some(tab) =>
        root.getSelectionModel.select(tab)
      case None =>
        HomeController.$this.insertTab(notFind())
    }

}