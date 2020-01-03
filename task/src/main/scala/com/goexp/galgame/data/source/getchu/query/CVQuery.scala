package com.goexp.galgame.data.source.getchu.query

import com.goexp.common.db.mongo.DBQueryTemplate
import com.goexp.galgame.common.db.mongo.query.CVCreator
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.data.source.getchu.DB_NAME

object CVQuery {
  private val tlp = DBQueryTemplate[CV](DB_NAME, "cv", CVCreator).build

  def apply() = tlp
}