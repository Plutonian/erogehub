package com.goexp.galgame.gui.db.mongo.query

import com.goexp.common.db.mongo.DBQueryTemplate
import com.goexp.galgame.common.db.mongo.query.CVCreator
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.gui.db.mongo.DB_NAME

object CVQuery {
  private val tpl = new DBQueryTemplate[CV](DB_NAME, "cv", CVCreator).build

  def apply() = tpl
}
