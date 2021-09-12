package com.goexp.galgame.gui.util

import javafx.beans.property.Property

import scala.collection.mutable

trait Controller extends DataSource {
  private val listeners = mutable.Set[Property[_]]()

  def registestListener(prop: Property[_]): Unit = {
    listeners.add(prop)
  }

  private def disposeListener(): Unit = {
    listeners.foreach { p =>
      p.unbind()
    }
  }

  def dispose(): Unit = {
    disposeListener()
  }
}
