package com.goexp.galgame.common.db.mongo.query

import com.goexp.common.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.{BrandType, CommonBrand}
import org.bson.Document
import org.slf4j.LoggerFactory

class CommonBrandCreator(private[this] val brand: CommonBrand) extends ObjectCreator[CommonBrand] {
  private lazy val logger = LoggerFactory.getLogger(classOf[CommonBrandCreator])

  override def create(doc: Document): CommonBrand = {
    brand.id = doc.getInteger("_id")
    brand.name = doc.getString("name")
    brand.website = doc.getString("website")
    brand.comp = doc.getString("comp")
    brand.isLike = BrandType.from(doc.getInteger("type"))

    logger.debug("{}", brand)


    brand
  }
}

