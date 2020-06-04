package com.goexp.galgame.gui.db.mongo.query

import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.db.mongo.query.CVCreator
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.gui.db.mongo.DB_NAME
import com.typesafe.scalalogging.Logger
import org.bson.Document

object CVQuery {
  private val logger = Logger(CVQuery.getClass)

  private val creator: ObjectCreator[CV] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val cv = CVCreator.create(doc)

    cv.statistics = Option(doc.get("statistics").asInstanceOf[Document]).map(StatCreators.statisticsCreator.create).orNull

    logger.trace(s"<cv> ${cv}")

    cv

  }

  private val tpl = DBQuery[CV](Config.DB_STRING, DB_NAME, "cv", creator).build

  def apply() = tpl


}
