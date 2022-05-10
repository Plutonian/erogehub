package com.goexp.galgame.gui.view.game.explorer.sidebar

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{BrandItem, CompItem, DataItem, DateItem, SampleItem}
import scalafx.beans.property.BooleanProperty
import scala.jdk.CollectionConverters._
import java.util.function.Predicate
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

  var groupPredicate: Predicate[Game] = _

  def reset() = {
    date = null
    brand = null
    cv = null
    tag = null
  }

  def makeBrandPredicate() = {

    groupPredicate = if (brand != null) {

      brand match {
        case CompItem(_, _, acomp) =>
          (game: Game) => {

            val comp = Option(game.brand.comp).getOrElse("")
            comp == acomp
          }
        case BrandItem(_, _, abrand) =>
          (game: Game) => game.brand == abrand
      }
    } else null

  }

  def makeDatePredicate() = {

    groupPredicate = if (date != null) {
      date match {
        case DateItem(_, range, _, _) =>
          (game: Game) =>
            game.publishDate != null &&
              game.publishDate.isBefore(range.end) &&
              game.publishDate.isAfter(range.start)
      }
    } else null

  }

  def makeCVPredicate() = {

    groupPredicate = if (cv != null) {
      cv match {
        case SampleItem(title, _) =>
          (g: Game) => Option(g.gameCharacters).exists(_.asScala.exists(_.getShowCV().exists(_ == title)))

      }
    } else null

  }

  var DEFAULT_GAME_PREDICATE: Predicate[Game] = (g: Game) => !GameState.ignoreState().contains(g.state.get)

  def mergeDefaultPredicate(p: Predicate[Game]) = {
    mergePredicate(DEFAULT_GAME_PREDICATE, p)
  }

  def mergePredicate(p1: Predicate[Game], p2: Predicate[Game]): Predicate[Game] = {
    if (p1 != null && p2 != null) {
      p1.and(p2)
    } else if (p1 != null) p1
    else if (p2 != null) p2
    else null
  }
}
