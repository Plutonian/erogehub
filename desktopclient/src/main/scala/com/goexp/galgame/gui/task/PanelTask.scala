package com.goexp.galgame.gui.task

import java.time.LocalDate
import java.util

import com.goexp.common.util.Strings
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.view.game.listview.sidebar.node.{BrandItemNode, CompItemNode, DateItemNode, DefaultItemNode}
import javafx.concurrent.Task
import javafx.scene.control.{Label, TreeItem}
import javafx.scene.layout.HBox
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object PanelTask {

  class GroupTag(val groupGames: util.List[Game]) extends Task[util.List[HBox]] {
    private[task] val logger = LoggerFactory.getLogger(getClass)

    override protected def call = createTagGroup(groupGames)

    private def createTagGroup(filteredGames: util.List[Game]) = {
      filteredGames.asScala.toStream
        .filter(g => g.tag.size > 0)
        .flatMap(g => g.tag.asScala.toStream.filter(t => Strings.isNotEmpty(t)))

        //Stream{String,String,String,String...}
        .groupBy(s => s).toStream
        .sortBy({ case (_, v) => v.size }).reverse
        .take(20)
        .map({ case (key, value) => {
          logger.debug(s"<createTagGroup> Name:$key,Value:${value.size}")
          new HBox(Tags.toNodes(key), new Label(s"(${value.size})"))
        }
        }).asJava
    }

  }

  class GroupDate(val groupGames: util.List[Game]) extends Task[TreeItem[DateItemNode]] {
    override protected def call: TreeItem[DateItemNode] = createDateGroup(groupGames)

    private def createDateGroup(filteredGames: util.List[Game]) = {
      val yearsStream = filteredGames.asScala.toStream
        .filter(game => game.publishDate != null)
        .groupBy(game => Option(game.publishDate).map(date => date.getYear).getOrElse(0)).toStream
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

          val monthNode = games.groupBy(game => Option(game.publishDate).map(date => date.getMonthValue).getOrElse(0)).toStream
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
      root
    }
  }

  class GroupBrand(val groupGames: util.List[Game]) extends Task[TreeItem[DefaultItemNode]] {
    override protected def call: TreeItem[DefaultItemNode] = createCompGroup(groupGames)

    private def createCompGroup(filteredGames: util.List[Game]) = {
      val compsNodes = filteredGames.asScala.toStream
        .groupBy(game => Option(game.brand.comp).getOrElse("")).toStream
        .sortBy({ case (_, v) => v.size }).reverse
        .map({ case (comp, v) => {

          val compNode = new TreeItem[DefaultItemNode](new CompItemNode(comp, v.size, comp))

          val brandNodes = v.groupBy(g => g.brand).toStream
            .map({ case (brand, games) => new TreeItem[DefaultItemNode](new BrandItemNode(brand.name, games.size, brand)) })
            .asJava

          compNode.getChildren.addAll(brandNodes)

          compNode

        }
        })
        .asJava

      val root = new TreeItem[DefaultItemNode]
      root.getChildren.setAll(compsNodes)
      root
    }
  }

}