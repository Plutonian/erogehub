package com.goexp.galgame.data.source.getchu.query

import com.goexp.db.mongo.DBQuery
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.db.mongo.query.CVCreator
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.data.source.getchu.DB_NAME

object CVQuery {
  private val tlp = DBQuery[CV](Config.DB_STRING, DB_NAME, "cv", CVCreator).build

  def apply() = tlp
}