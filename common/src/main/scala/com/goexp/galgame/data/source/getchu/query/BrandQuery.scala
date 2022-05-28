package com.goexp.galgame.data.source.getchu.query

import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CommonBrandCreator
import com.goexp.galgame.common.model.brand.BrandState
import com.goexp.galgame.data.model.Brand
import com.typesafe.scalalogging.Logger
import org.bson.Document

object BrandQuery {
  private val tlp = DBQuery[Brand](Config.DB_STRING, DB_NAME, "brand", Creator).build

  def apply() = tlp

  private object Creator extends ObjectCreator[Brand] {
    private val logger = Logger(Creator.getClass)

    override def create(doc: Document): Brand = {
      logger.trace(s"<create> doc=${doc}")

      val parentCreator = new CommonBrandCreator(new Brand)
      val b = parentCreator.create(doc).asInstanceOf[Brand]

      b.state = BrandState.from(doc.getInteger("type"))

      b
    }
  }

}