package com.goexp.galgame.data.db.query.mongdb

import com.goexp.common.db.mongo.DBQueryTemplate
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.db.mongo.query.GuideCreator
import com.goexp.galgame.common.model.CommonGame

object GuideQuery {
  lazy val tlp = new DBQueryTemplate.Builder[CommonGame.Guide](DB_NAME, "guide", GuideCreator).build

}