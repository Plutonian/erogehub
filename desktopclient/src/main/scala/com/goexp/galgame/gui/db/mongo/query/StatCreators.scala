package com.goexp.galgame.gui.db.mongo.query

import com.goexp.common.util.date.DateUtil
import com.goexp.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.{GameStatistics, LocationStatistics, StarStatistics, StateStatistics}
import com.typesafe.scalalogging.Logger
import org.bson.Document

object StatCreators {
  private val logger = Logger(StatCreators.getClass)


  val statisticsCreator: ObjectCreator[GameStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = GameStatistics(
      Option(doc.getDate("start")).map(DateUtil.toLocalDate).orNull,
      Option(doc.getDate("end")).map(DateUtil.toLocalDate).orNull,
      doc.getInteger("count", 0),
      doc.getInteger("realCount", 0),
      Option(doc.get("state").asInstanceOf[Document]).map(stateCreator.create).orNull,
      Option(doc.get("star").asInstanceOf[Document]).map(starCreator.create).orNull,
      Option(doc.get("location").asInstanceOf[Document]).map(locCreator.create).orNull
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
      doc.getInteger("four"),
      doc.getInteger("five")
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }
  private val locCreator: ObjectCreator[LocationStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = LocationStatistics(
      doc.getInteger("local"),
      doc.getInteger("netdisk"),
      doc.getInteger("remote")
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }
}
