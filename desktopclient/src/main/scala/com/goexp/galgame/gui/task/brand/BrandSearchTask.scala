package com.goexp.galgame.gui.task.brand

import java.util

import com.goexp.common.util.Strings
import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.gui.db.mongo.Query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.regex
import javafx.concurrent.Task
import javafx.scene.control.TreeItem

import scala.collection.JavaConverters._

object BrandSearchTask {
  private def makeTree(brands: util.List[Brand]) =

    brands.asScala.toStream
      .groupBy(b => if (Strings.isNotEmpty(b.comp)) b.comp else "").toStream
      .sortBy({ case (key, _) => key })

      .map({ case (k, v) =>
        val comp = new Brand
        comp.comp = k
        val rootItem = new TreeItem[Brand]
        rootItem.setValue(comp)
        rootItem.setExpanded(true)

        val brandNodes = v.map(brand => {
          val item = new TreeItem[Brand]
          item.setValue(brand)
          item
        }).asJava

        rootItem.getChildren.setAll(brandNodes)
        rootItem
      })
      .asJava

  class ByName(private[this] val name: String) extends Task[util.List[TreeItem[Brand]]] {
    override protected def call = {
      val list = BrandQuery.tlp.query.where(regex("name", "^" + name)).list
      makeTree(list)
    }
  }

  class ByType(private[this] val `type`: BrandType) extends Task[util.List[TreeItem[Brand]]] {
    override protected def call = {
      val list = if (`type` eq BrandType.ALL)
        BrandQuery.tlp.query.list
      else
        BrandQuery.tlp.query.where(Filters.eq("type", `type`.value)).list

      makeTree(list)
    }
  }

  class ByComp(private[this] val name: String) extends Task[util.List[TreeItem[Brand]]] {
    override protected def call = {
      val list = BrandQuery.tlp.query.where(regex("comp", name)).list
      makeTree(list)
    }
  }

}