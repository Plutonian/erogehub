package com.goexp.galgame.gui.db.mongo.query

import com.goexp.common.db.mongo.DBQueryTemplate
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.db.mongo.query.GuideCreator
import com.goexp.galgame.common.model._
import com.mongodb.client.model.Sorts.ascending

object GuideQuery {
  //    private val logger = LoggerFactory.getLogger(GuideQuery.getClass)

  val tlp = new DBQueryTemplate.Builder[CommonGame.Guide](DB_NAME, "guide", GuideCreator)
    .defaultSort(ascending("title"))
    .build

}
