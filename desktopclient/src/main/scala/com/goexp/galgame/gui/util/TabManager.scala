package com.goexp.galgame.gui.util

import com.goexp.galgame.gui.view.game.HomeController
import javafx.scene.control.{Tab, TabPane}

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object TabManager {
  def apply(root: TabPane): TabManager = new TabManager(root)

  def apply(): TabManager = TabManager(HomeController.tabPanel)

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
        t.setUserData(key)

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

  def open(key: String, tab: => Tab with Controller): Unit = {
    tabs.get(key) match {
      case Some((tab, _)) =>
        root.getSelectionModel.select(tab)
      case None =>
        val t = tab
        t.setUserData(key)

        t.setOnClosed { _ =>
          tab.dispose()
          t.setOnClosed(null)
          tabs.remove(key)
        }

        val index = root.getSelectionModel.getSelectedIndex
        root.getTabs.add(index + 1, t)
        root.getSelectionModel.select(t)

        tabs.addOne(key, (t, () => t.load()))

        t.load()
    }
  }

  private def close(tab: Tab) = {
    tab match {
      case t: Tab with Controller => t.dispose()
      case _ =>
    }

    //remove from cache
    tabs.remove(tab.getUserData.asInstanceOf[String])
    root.getTabs.remove(tab)
  }

  def closeOther() = {
    root.getTabs.asScala
      .filter(_ ne root.getSelectionModel.getSelectedItem)
      .foreach {
        close
      }
  }

  def closeLeft() = {
    val index = root.getSelectionModel.getSelectedIndex

    if (index > 0) {
      (0 until index).foreach { i =>

        val t = root.getTabs.get(0)

        close(t)
      }
    }


  }

  def closeRight() = {

    val index = root.getSelectionModel.getSelectedIndex
    val total = root.getTabs.size

    if (index < total - 1) {
      (index + 1 until total).foreach { i =>
        val t = root.getTabs.get(index + 1)

        close(t)
      }
    }
  }


  def reloadActiveTabData() = {
    val key = root.getSelectionModel.getSelectedItem.getUserData.asInstanceOf[String]

    val (_, reload) = tabs(key)

    reload()
  }

}