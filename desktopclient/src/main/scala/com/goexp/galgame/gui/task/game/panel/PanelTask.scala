package com.goexp.galgame.gui.task.game.panel

import java.time.LocalDate
import java.util

import com.goexp.common.util.Strings
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.panel.node._
import javafx.concurrent.Task
import javafx.scene.control.TreeItem
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

object PanelTask {

  class GroupTag(val groupGames: util.List[Game]) extends Task[util.List[DefaultItemNode]] {
    private[task] val logger = LoggerFactory.getLogger(getClass)

    override protected def call: util.List[DefaultItemNode] = createTagGroup(groupGames)

    private def createTagGroup(filteredGames: util.List[Game]) = {
      filteredGames.asScala.to(LazyList)
        .filter(g => g.tag.size > 0)
        .flatMap(g => g.tag.asScala.to(LazyList).filter(t => Strings.isNotEmpty(t)))

        //Stream{String,String,String,String...}
        .groupBy(s => s).to(LazyList)
        .sortBy({ case (_, v) => v.size }).reverse
//        .take(20)
        .map({ case (key, value) =>
          logger.debug(s"<createTagGroup> Name:$key,Value:${value.size}")
          new DefaultItemNode(key, value.size)
        }).asJava
    }

  }

  class GroupCV(val groupGames: util.List[Game]) extends Task[util.List[DefaultItemNode]] {
    private[task] val logger = LoggerFactory.getLogger(getClass)

    override protected def call: util.List[DefaultItemNode] = createTagGroup(groupGames)

    private def createTagGroup(filteredGames: util.List[Game]) = {
      filteredGames.asScala.to(LazyList)
        .filter(g => Option(g.gameCharacters).map(_.size()).getOrElse(0) > 0)
        .flatMap(g =>
          g.gameCharacters.asScala.to(LazyList)
            .map(p => if (Strings.isNotEmpty(p.trueCV)) p.trueCV else if (Strings.isNotEmpty(p.cv)) p.cv else "")
            .filter(t => Strings.isNotEmpty(t))
        )

        //Stream{String,String,String,String...}
        .groupBy(s => s).to(LazyList)
        .sortBy({ case (_, v) => v.size }).reverse
        //        .take(20)
        .map({ case (key, value) =>
          logger.debug(s"<createTagGroup> Name:$key,Value:${value.size}")
          new DefaultItemNode(key, value.size)

        }).asJava
    }

  }

  class GroupDate(val groupGames: util.List[Game]) extends Task[TreeItem[DateItemNode]] {
    override protected def call: TreeItem[DateItemNode] = createDateGroup(groupGames)

    private def createDateGroup(filteredGames: util.List[Game]) = {
      val yearsStream = filteredGames.asScala.to(LazyList)
        .filter(game => game.publishDate != null)
        .groupBy(game => Option(game.publishDate).map(date => date.getYear).getOrElse(0)).to(LazyList)
        .filter({ case (year, _) => year != 0 })
        .sortBy({ case (k, _) => k }).reverse
        .map({ case (year, games) =>

          val yearNode = {
            new TreeItem[DateItemNode](new DateItemNode(
              s"$year (${games.size})",
              LocalDate.of(year, 1, 1).minusDays(1),
              LocalDate.of(year, 12, 31).plusDays(1),
              games.size,
              DateItemNode.DateType.YEAR))
          }

          val monthNode = games.groupBy(game => Option(game.publishDate).map(date => date.getMonthValue).getOrElse(0)).to(LazyList)
            .sortBy({ case (k, _) => k }).reverse
            .map({ case (month, v) =>
              new TreeItem[DateItemNode](new DateItemNode(s"$month 月 (${v.size})",
                LocalDate.of(year, month, 1).minusDays(1),
                LocalDate.of(year, month, 1).plusMonths(1),
                v.size, DateItemNode.DateType.MONTH))

            }).asJava

          yearNode.getChildren.addAll(monthNode)
          yearNode

        }).asJava


      val root = new TreeItem[DateItemNode]
      root.getChildren.setAll(yearsStream)

      // only one
      if (yearsStream.size == 1)
        yearsStream.get(0).setExpanded(true)

      root
    }
  }

  class GroupBrand(val groupGames: util.List[Game]) extends Task[TreeItem[DefaultItemNode]] {
    override protected def call: TreeItem[DefaultItemNode] = createCompGroup(groupGames)

    private def createCompGroup(filteredGames: util.List[Game]) = {
      val compsNodes = filteredGames.asScala.to(LazyList)
        .groupBy(game => Option(game.brand.comp).getOrElse(game.brand)).to(LazyList)
        .sortBy({ case (_, v) => v.size }).reverse
        .map({
          case (comp: String, v) =>

            val brandNodes = v.groupBy(g => g.brand).to(LazyList)
              .map({ case (brand, games) => new TreeItem[DefaultItemNode](new BrandItemNode(brand.name, games.size, brand)) })
              .asJava

            if (brandNodes.size > 1) {
              val compNode = new TreeItem[DefaultItemNode](new CompItemNode(comp, v.size, comp))
              compNode.getChildren.addAll(brandNodes)

              compNode
            } else {
              brandNodes.get(0)
            }
          case (brand: Brand, v) =>

            val compNode = new TreeItem[DefaultItemNode](new BrandItemNode(brand.name, v.size, brand))
            compNode
        })
        .asJava

      val root = new TreeItem[DefaultItemNode]
      root.getChildren.setAll(compsNodes)
      root
    }
  }

}