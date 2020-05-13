package com.goexp.galgame.data.script.source.getchu.local.cal

import com.goexp.galgame.common.model.{GameStatistics, LocationStatistics, StarStatistics, StateStatistics}
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.data.model.Game

object GameStat {
  def calStat(games: LazyList[Game]) = {
    val count = games.size
    val start = games.filter(_.publishDate != null).map(_.publishDate).minOption
    val end = games.filter(_.publishDate != null).map(_.publishDate).maxOption

    val filterdList = games.filter { g => (g.state ne GameState.SAME) && (g.state ne GameState.BLOCK) }

    val realCount = filterdList.size


    val statMap = filterdList.groupBy { g => g.state }.to(LazyList).map { case (k, v) => (k, v.size) }.toMap
    val starMap = filterdList.groupBy { g => g.star }.to(LazyList).map { case (k, v) => (k, v.size) }.toMap
    val locationMap = filterdList.groupBy { g => g.location }.to(LazyList).map { case (k, v) => (k, v.size) }.toMap


    val statistics =
      GameStatistics(
        start.orNull,
        end.orNull,
        count,
        realCount,
        StateStatistics(
          statMap.getOrElse(GameState.PLAYED, 0),
          statMap.getOrElse(GameState.PLAYING, 0),
          statMap.getOrElse(GameState.HOPE, 0),
          statMap.getOrElse(GameState.READYTOVIEW, 0),
          statMap.getOrElse(GameState.UNCHECKED, 0)
        ), StarStatistics(
          starMap.getOrElse(0, 0),
          starMap.getOrElse(1, 0),
          starMap.getOrElse(2, 0),
          starMap.getOrElse(3, 0),
          starMap.getOrElse(4, 0),
          starMap.getOrElse(5, 0)
        ), LocationStatistics(
          locationMap.getOrElse(GameLocation.LOCAL, 0),
          locationMap.getOrElse(GameLocation.NETDISK, 0),
          locationMap.getOrElse(GameLocation.REMOTE, 0)
        )
      )
    statistics
  }
}
