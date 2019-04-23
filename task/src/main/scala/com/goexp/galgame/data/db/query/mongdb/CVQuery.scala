package com.goexp.galgame.data.db.query.mongdb

import com.goexp.common.db.mongo.DBQueryTemplate
import com.goexp.galgame.common.db.mongo.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CVCreator
import com.goexp.galgame.common.model.CV

object CVQuery {
  lazy val tlp = new DBQueryTemplate.Builder[CV](DB_NAME, "cv", CVCreator).build
}