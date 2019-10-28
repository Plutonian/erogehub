package com.goexp.galgame.gui.task.game.panel.group

import java.time.LocalDate
import java.util

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{DateItem, DateType}
import javafx.concurrent.Task
import javafx.scene.control.TreeItem

import scala.jdk.CollectionConverters._

class ByDate(val groupGames: util.List[Game]) extends Task[TreeItem[DateItem]] {
  override protected def call: TreeItem[DateItem] = createDateGroup(groupGames)

  private def createDateGroup(filteredGames: util.List[Game]) = {
    val yearsStream = filteredGames.asScala.to(LazyList)
      .filter(game => game.publishDate != null)
      .groupBy(game => Option(game.publishDate).map(date => date.getYear).getOrElse(0)).to(LazyList)
      .filter({ case (year, _) => year != 0 })
      .sortBy({ case (k, _) => k }).reverse
      .map({ case (year, games) =>

        val yearNode = {
          new TreeItem[DateItem](new DateItem(
            s"$year (${games.size})",
            LocalDate.of(year, 1, 1).minusDays(1),
            LocalDate.of(year, 12, 31).plusDays(1),
            games.size,
            DateType.YEAR))
        }

        val monthNode = games.groupBy(game => Option(game.publishDate).map(date => date.getMonthValue).getOrElse(0)).to(LazyList)
          .sortBy({ case (k, _) => k }).reverse
          .map({ case (month, v) =>
            new TreeItem[DateItem](new DateItem(s"$month 月 (${v.size})",
              LocalDate.of(year, month, 1).minusDays(1),
              LocalDate.of(year, month, 1).plusMonths(1),
              v.size, DateType.MONTH))

          }).asJava

        yearNode.getChildren.addAll(monthNode)
        yearNode

      }).asJava


    val root = new TreeItem[DateItem]
    root.getChildren.setAll(yearsStream)

    // only one
    if (yearsStream.size == 1)
      yearsStream.get(0).setExpanded(true)

    root
  }
}
