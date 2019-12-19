package com.goexp.javafx.cell

import javafx.scene.Node

class TreeCell[Entity] extends javafx.scene.control.TreeCell[Entity] {
  override protected def updateItem(item: Entity, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    if (item != null && !empty)
      notEmpty(item)
    else
      whenEmpty()

  }

  protected def notEmpty(item: Entity): Unit = {

  }

  protected def whenEmpty(): Unit = {

  }

}

object TextTreeCell {
  def apply[Entity](hasData: Entity => String) = new TreeCell[Entity]() {
    override def notEmpty(item: Entity): Unit = {
      this.setText(hasData(item))
    }
  }

}

object NodeTreeCell {

  def apply[Entity](hasData: Entity => Node) = new TreeCell[Entity]() {
    override def notEmpty(item: Entity): Unit = {
      this.setGraphic(hasData(item))
    }
  }
}
