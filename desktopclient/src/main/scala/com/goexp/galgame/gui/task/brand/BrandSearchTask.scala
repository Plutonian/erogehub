package com.goexp.galgame.gui.task.brand

import java.util

import com.goexp.common.util.Strings
import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.gui.db.mongo.Query.BrandQuery
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.regex
import javafx.collections.{FXCollections, ObservableList}
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

  class ByName(name: String) extends Task[ObservableList[TreeItem[Brand]]] {
    override protected def call: ObservableList[TreeItem[Brand]] = {
      val list = BrandQuery.tlp.query.where(regex("name", "^" + name)).list
      FXCollections.observableArrayList(makeTree(list))
    }
  }

  class ByType(`type`: BrandType) extends Task[ObservableList[TreeItem[Brand]]] {
    override protected def call: ObservableList[TreeItem[Brand]] = {
      val list = if (`type` eq BrandType.ALL)
        BrandQuery.tlp.query.list
      else
        BrandQuery.tlp.query.where(Filters.eq("type", `type`.getValue)).list

      FXCollections.observableArrayList(makeTree(list))
    }
  }

  class ByComp(name: String) extends Task[ObservableList[TreeItem[Brand]]] {
    override protected def call: ObservableList[TreeItem[Brand]] = {
      val list = BrandQuery.tlp.query.where(regex("comp", name)).list
      FXCollections.observableArrayList(makeTree(list))
    }
  }

}