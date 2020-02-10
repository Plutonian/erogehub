package com.goexp.galgame.gui.view.game.listview.sidebar

import java.util
import java.util.function.Predicate

import com.goexp.ui.javafx.DefaultController
import javafx.beans.property.SimpleBooleanProperty

abstract class FilterController[T] extends DefaultController {
  final val onSetProperty = new SimpleBooleanProperty(false)
  var predicate: Predicate[T] = _

  def init(filteredGames: util.List[T]): Unit
}