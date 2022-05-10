package com.goexp.galgame.gui.view.game.explorer.sidebar

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.task.game.panel.group.node.DataItem
import scalafx.beans.property.BooleanProperty

import scala.collection.mutable

object FilterCondition {
  var date: DataItem = _

  var brand: DataItem = _

  var cv: DataItem = _

  var tag: DataItem = _


  val _selectedStar = mutable.Set[Int](0, 1, 2, 3, 4, 5)
  val _selectedGameState = mutable.Set[GameState]()
    .addAll(GameState.values.filter(_.value > GameState.BLOCK.value))

  val _selectedGameLocation = mutable.Set[GameLocation](
    GameLocation.REMOTE,
    GameLocation.LOCAL)

  val _switchAll = new BooleanProperty
}
