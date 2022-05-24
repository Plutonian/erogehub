package com.goexp.galgame.gui.view.game.explorer.sidebar

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.DateRange
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node._
import com.goexp.galgame.gui.view.game.explorer.sidebar.FilterCondition.FilterUtils.{DEFAULT_GAME_FILTER, mergeFilter}
import com.goexp.galgame.gui.view.game.explorer.sidebar.FilterCondition.{DEFAULT_GAME_PREDICATE, mergePredicate}
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.{and, gte, lte}
import org.bson.conversions.Bson

import java.time.LocalDate
import java.util.function.Predicate
import scala.collection.mutable
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

object FilterCondition {

  object FilterUtils {
    var DEFAULT_GAME_FILTER = and(
      Filters.nin("state", Set(GameState.BLOCK, GameState.SAME).map(_.value).asJava),
      Filters.nin("star", Set(1, 2).asJava)
    )


    def mergeDefaultFilter(p: Bson) = {
      mergeFilter(DEFAULT_GAME_FILTER, p)
    }

    def mergeFilter(p1: Bson, p2: Bson) = {
      if (p1 != null && p2 != null) {
        and(p1, p2)
      } else if (p1 != null) p1
      else if (p2 != null) p2
      else null
    }

  }


  var DEFAULT_GAME_PREDICATE: Predicate[Game] = (g: Game) => !GameState.ignoreState().contains(g.state.get) && !Set(1, 2).contains(g.star.intValue())


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

class FilterCondition {
  var date: DataItem = _
  var brand: DataItem = _
  var cv: DataItem = _
  var tag: DataItem = _

  var _selectedStar: mutable.Set[Int] = _
  var _selectedGameState: mutable.Set[GameState] = _
  var _selectedGameLocation: mutable.Set[GameLocation] = _

  var _switchAll: Boolean = false

  var groupPredicate: Predicate[Game] = _
  var filterPredicate: Predicate[Game] = DEFAULT_GAME_PREDICATE

  def reset() = {
    date = null
    brand = null
    cv = null
    tag = null

    _selectedStar = null
    _selectedGameState = null
    _selectedGameLocation = null
  }

  object FilterUtil {

    var groupFilter: Bson = _
    var filterFilter: Bson = DEFAULT_GAME_FILTER

    def makeBrandFilter() = {

      groupFilter = if (brand != null) {

        brand match {
          //          case CompItem(_, _, acomp) => {
          //            null
          //          }

          case BrandItem(_, _, abrand) =>
            Filters.eq("brandId", abrand.id)

          case _ => null
        }
      } else null

    }

    def makeDateFilter() = {

      groupFilter = if (date != null) {
        date match {
          case DateItem(_, DateRange(start, end), _, _) =>
            println(s"$start - $end")


            and(
              gte("publishDate", DateUtil.toDate(s"${start.toString} 00:00:00")),
              lte("publishDate", DateUtil.toDate(s"${end.toString} 23:59:59"))
            )
        }
      } else null

    }

    def makeCVFilter() = {

    }


    def makeFilterFilter() = {

      val p = and(
        Filters.in("star", _selectedStar.asJava),
        Filters.in("state", _selectedGameState.map(_.value).asJava),
        Filters.in("location", _selectedGameLocation.map(_.value).asJava),
      )

      val p2 = lte("publishDate", DateUtil.toDate(s"${LocalDate.now().toString} 23:59:59"))

      filterFilter =
        if (_switchAll) {
          mergeFilter(p, p2)
        } else {
          p
        }
    }

    def finalFilter() = {
      mergeFilter(groupFilter, filterFilter)
    }
  }

  def makeBrandPredicate() = {

    groupPredicate = if (brand != null) {

      brand match {
        //        case CompItem(_, _, acomp) =>
        //          (game: Game) => {
        //
        //            val comp = Option(game.brand.comp).getOrElse("")
        //            comp == acomp
        //          }
        case BrandItem(_, _, abrand) =>
          (game: Game) => game.brand == abrand

        case _ => null
      }
    } else null

  }

  def makeDatePredicate() = {

    groupPredicate = if (date != null) {
      date match {
        case DateItem(_, range, _, _) =>
          (game: Game) =>
            game.publishDate != null &&
            (game.publishDate.isEqual(range.end) ||
              game.publishDate.isBefore(range.end)) && (
              game.publishDate.isEqual(range.start) ||
                game.publishDate.isAfter(range.start)
              )
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


  def makeFilterPredicate() = {

    val p: Predicate[Game] = (game: Game) => {
      _selectedStar.contains(game.star.get()) &&
        _selectedGameState.contains(game.state.get) &&
        _selectedGameLocation.contains(game.location.get)
    }
    val p2: Predicate[Game] = (game: Game) =>
      Option(game.publishDate).exists {
        _.isBefore(LocalDate.now())
      }

    filterPredicate =
      if (_switchAll) {
        mergePredicate(p, p2)
      } else {
        p
      }
  }

  def finalPredicate() = {
    mergePredicate(groupPredicate, filterPredicate)
  }

}
