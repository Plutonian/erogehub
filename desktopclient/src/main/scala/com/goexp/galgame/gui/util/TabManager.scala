package com.goexp.galgame.gui.util

import com.goexp.galgame.gui.view.MainController
import javafx.scene.control.{Tab, TabPane}

import scala.collection.mutable

object TabManager {
  def apply(root: TabPane): TabManager = new TabManager(root)

  def apply(): TabManager = new TabManager(MainController().homeController.mainTabPanel)

  type TabAndReload = (Tab, () => Unit)

  val tabs = mutable.Map[String, TabAndReload]()
}

//class TabConfig() {
//  var key: String = _
//  var tab: () => Tab = _
//  var reload: () => Unit = _
//
//}

class TabManager private(val root: TabPane) {

  import TabManager.tabs

  def open(key: String, tab: => Tab)(load: => Unit): Unit = {
    tabs.get(key) match {
      case Some((tab, _)) =>
        root.getSelectionModel.select(tab)
      case None =>
        val t = tab

        t.setOnClosed { _ =>
          tabs.remove(key)
        }

        val index = root.getSelectionModel.getSelectedIndex
        root.getTabs.add(index + 1, t)
        root.getSelectionModel.select(t)

        tabs.addOne(key, (t, () => load))

        load
    }
  }

}