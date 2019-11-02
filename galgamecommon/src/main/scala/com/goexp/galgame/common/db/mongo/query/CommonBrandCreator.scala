package com.goexp.galgame.common.db.mongo.query

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.game.brand.{BrandType, CommonBrand}
import org.bson.Document
import com.typesafe.scalalogging.Logger

class CommonBrandCreator(
                          private[this] val brand: CommonBrand
                        ) extends ObjectCreator[CommonBrand] {

  private val logger = Logger(classOf[CommonBrandCreator])

  override def create(doc: Document): CommonBrand = {
    logger.debug("<Doc>{}", doc)


    brand.id = doc.getInteger("_id")
    brand.name = doc.getString("name")
    brand.website = doc.getString("website")
    brand.comp = doc.getString("comp")
    brand.isLike = BrandType.from(doc.getInteger("type"))

    logger.debug("<brand>{}", brand)


    brand
  }
}

