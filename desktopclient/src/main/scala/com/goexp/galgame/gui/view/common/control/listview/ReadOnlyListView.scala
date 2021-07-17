package com.goexp.galgame.gui.view.common.control.listview

import javafx.scene.control.{ListView, Skin}


class ReadOnlyListView[T] extends ListView[T] {
  override def createDefaultSkin(): Skin[_] = new ReadOnlyListViewSkin[T](this)
}
