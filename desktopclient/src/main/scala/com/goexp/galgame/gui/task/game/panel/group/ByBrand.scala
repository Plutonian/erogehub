package com.goexp.galgame.gui.task.game.panel.group

import java.util

import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.panel.group.node._
import javafx.concurrent.Task
import javafx.scene.control.TreeItem

import scala.jdk.CollectionConverters._

class ByBrand(val groupGames: util.List[Game]) extends Task[TreeItem[DefaultItem]] {
  override protected def call: TreeItem[DefaultItem] = createCompGroup(groupGames)

  private def createCompGroup(filteredGames: util.List[Game]) = {
    val compsNodes = filteredGames.asScala.to(LazyList)
      .groupBy(game => Option(game.brand.comp).getOrElse(game.brand)).to(LazyList)
      .sortBy({ case (_, v) => v.size }).reverse
      .map({
        case (comp: String, v) =>

          val brandNodes = v.groupBy(g => g.brand).to(LazyList)
            .map({ case (brand, games) => new TreeItem[DefaultItem](new BrandItem(brand.name, games.size, brand)) })
            .asJava

          if (brandNodes.size > 1) {
            val compNode = new TreeItem[DefaultItem](new CompItem(comp, v.size, comp))
            compNode.getChildren.addAll(brandNodes)

            compNode
          } else {
            brandNodes.get(0)
          }
        case (brand: Brand, v) =>

          val compNode = new TreeItem[DefaultItem](new BrandItem(brand.name, v.size, brand))
          compNode
      })
      .asJava

    val root = new TreeItem[DefaultItem]
    root.getChildren.setAll(compsNodes)
    root
  }
}