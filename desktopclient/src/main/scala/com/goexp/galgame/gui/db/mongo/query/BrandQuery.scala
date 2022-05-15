package com.goexp.galgame.gui.db.mongo.query

import java.util
import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.db.mongo.query.CommonBrandCreator
import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.db.mongo.DB_NAME
import com.goexp.galgame.gui.db.mongo.query.StatCreators.statisticsCreator
import com.goexp.galgame.gui.model.Brand
import com.mongodb.client.model.Sorts.ascending
import com.typesafe.scalalogging.Logger
import org.bson.Document

import scala.jdk.CollectionConverters._


object BrandQuery {
  private val logger = Logger(BrandQuery.getClass)

  private val creator: ObjectCreator[Brand] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val parentCreator = new CommonBrandCreator(new Brand)
    val b = parentCreator.create(doc).asInstanceOf[Brand]

    b.state.set(BrandState.from(doc.getInteger("type")))
    b.tag = Option(doc.get("tag", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull
    b.statistics = Option(doc.get("statistics").asInstanceOf[Document]).map(statisticsCreator.create).orNull

    logger.trace(s"<brand> ${b}")

    b

  }


  private val tpl = DBQuery[Brand](Config.DB_STRING, DB_NAME, "brand", creator)
    .defaultSort(ascending("comp"))
    .build

  def apply() = tpl

}
