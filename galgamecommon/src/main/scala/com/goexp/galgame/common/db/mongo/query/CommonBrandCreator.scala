package com.goexp.galgame.common.db.mongo.query

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.game.brand.{BrandType, CommonBrand}
import com.typesafe.scalalogging.Logger
import org.bson.Document

class CommonBrandCreator(
                          private[this] val brand: CommonBrand
                        ) extends ObjectCreator[CommonBrand] {

  private val logger = Logger(classOf[CommonBrandCreator])

  override def create(doc: Document): CommonBrand = {
    logger.trace(s"<Doc> $doc")


    brand.id = doc.getInteger("_id")
    brand.name = doc.getString("name")
    brand.website = doc.getString("website")
    brand.comp = doc.getString("comp")
    brand.state = BrandType.from(doc.getInteger("type"))

    logger.trace(s"<brand> ${brand}")


    brand
  }
}

