package com.goexp.galgame.gui.task.game

import java.time.LocalDate
import java.util

import com.goexp.common.util.DateUtil
import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.gui.db.mongo.Query.{BrandQuery, GameQuery}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.AppCache
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import javafx.collections.{FXCollections, ObservableList}
import javafx.concurrent.Task

import scala.collection.JavaConverters._

object GameSearchTask {

  private def fillGameList(list: util.List[Game]) = {

    def fillGameWithBrand(g: Game): Game = {
      val key = g.brand.id
      g.brand = Option(AppCache.brandCache.get(key))
        .getOrElse {
          val brand = BrandQuery.tlp.query.where(Filters.eq(g.brand.id)).one
          AppCache.brandCache.put(key, brand)
          brand
        }
      g
    }

    list.asScala.toStream
      .distinct
      .map { g => fillGameWithBrand(g) }
      .asJava
  }

  class ByCV(cv: String, real: Boolean) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {
      val list = if (real)
        GameQuery.tlp.query.where(Filters.eq("gamechar.truecv", cv)).list
      else
        GameQuery.tlp.query.where(Filters.eq("gamechar.cv", cv)).list


      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByPainter(cv: String) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {

      val list = GameQuery.tlp.query
        .where(Filters.eq("painter", cv))
        .list

      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByDateRange(start: LocalDate, end: LocalDate) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {
      val list = GameQuery.tlp.query
        .where(and(
          gte("publishDate", DateUtil.toDate(start.toString + " 00:00:00")),
          lte("publishDate", DateUtil.toDate(end.toString + " 23:59:59"))))
        .list


      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByName(name: String) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {
      val list = GameQuery.tlp.query
        .where(regex("name", "^" + name))
        .list

      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByNameEx(name: String) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {
      val list = GameQuery.tlp.query
        .where(regex("name", name))
        .list


      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByTag(val tag: String) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {
      val list = GameQuery.tlp.query
        .where(Filters.eq("tag", tag))
        .list


      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByStarRange(begin: Int, end: Int) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {
      val list = GameQuery.tlp.query
        .where(and(
          gte("star", begin),
          lte("star", end)))
        .list

      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByState(gameState: GameState) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {
      val list = GameQuery.tlp.query
        .where(Filters.eq("state", gameState.getValue))
        .list

      FXCollections.observableArrayList(fillGameList(list))
    }
  }

  class ByBrand(brandId: Int) extends Task[ObservableList[Game]] {
    override protected def call: ObservableList[Game] = {

      val list = GameQuery.tlp.query
        .where(Filters.eq("brandId", brandId)).list

      FXCollections.observableArrayList(fillGameList(list))
    }
  }

}