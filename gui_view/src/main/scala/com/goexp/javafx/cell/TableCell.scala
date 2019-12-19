package com.goexp.javafx.cell

import javafx.scene.Node

class TableCell[Table, Entity] extends javafx.scene.control.TableCell[Table, Entity] {
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

object TextTableCell {
  def apply[Table, Entity](hasData: Entity => String) = new TableCell[Table, Entity]() {
    override def notEmpty(item: Entity): Unit = {
      this.setText(hasData(item))
    }
  }

}

object NodeTableCell {

  def apply[Table, Entity](hasData: Entity => Node) = new TableCell[Table, Entity]() {
    override def notEmpty(item: Entity): Unit = {
      this.setGraphic(hasData(item))
    }
  }
}
