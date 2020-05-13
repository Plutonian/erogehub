package com.goexp.galgame.gui.db.mongo.query

import java.util

import com.goexp.common.util.date.DateUtil
import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.db.mongo.query.CommonBrandCreator
import com.goexp.galgame.common.model.{GameStatistics, StarStatistics, StateStatistics}
import com.goexp.galgame.gui.db.mongo.DB_NAME
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

    b.tag = Option(doc.get("tag", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull
    b.statistics = Option(doc.get("statistics").asInstanceOf[Document]).map(statisticsCreator.create).orNull

    println(b.statistics)

    logger.trace(s"<brand> ${b}")

    b

  }


  private val statisticsCreator: ObjectCreator[GameStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = GameStatistics(
      Option(doc.getDate("start")).map(DateUtil.toLocalDate).orNull,
      Option(doc.getDate("end")).map(DateUtil.toLocalDate).orNull,
      doc.getInteger("count", 0),
      doc.getInteger("realCount", 0),
      Option(doc.get("state").asInstanceOf[Document]).map(stateCreator.create).orNull,
      Option(doc.get("star").asInstanceOf[Document]).map(starCreator.create).orNull
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }

  private val stateCreator: ObjectCreator[StateStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = StateStatistics(
      doc.getInteger("played"),
      doc.getInteger("playing"),
      doc.getInteger("hope"),
      doc.getInteger("viewLater"),
      doc.getInteger("uncheck")
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }

  private val starCreator: ObjectCreator[StarStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = StarStatistics(
      doc.getInteger("zero"),
      doc.getInteger("one"),
      doc.getInteger("two"),
      doc.getInteger("three"),
      doc.getInteger("fore"),
      doc.getInteger("five")
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }


  private val tpl = DBQuery[Brand](DB_NAME, "brand", creator)
    .defaultSort(ascending("comp"))
    .build

  def apply() = tpl

}
