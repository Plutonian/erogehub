package com.goexp.galgame.data.task.handler.starter

import com.goexp.galgame.data.db.query.mongdb.BrandQuery
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultStarter
import org.slf4j.LoggerFactory

class FromAllBrand extends DefaultStarter {
  private val logger = LoggerFactory.getLogger(classOf[FromAllBrand])

  override def process() = {
    BrandQuery.tlp.query.list.forEach(brand => {
      send(new Message[Int](99, brand.id))
    })
    logger.info("All Done!!!")
  }
}