package com.goexp.galgame.gui.db.mongo.query

import com.goexp.db.mongo.DBQuery
import com.goexp.galgame.common.db.mongo.query.GuideCreator
import com.goexp.galgame.common.model.game.guide.GameGuide
import com.goexp.galgame.gui.db.mongo.DB_NAME
import com.mongodb.client.model.Sorts.ascending

object GuideQuery {
  //    private val logger = Logger(GuideQuery.getClass)

  private val tpl = DBQuery[GameGuide](DB_NAME, "guide", GuideCreator)
    .defaultSort(ascending("title"))
    .build

  def apply() = tpl

}
