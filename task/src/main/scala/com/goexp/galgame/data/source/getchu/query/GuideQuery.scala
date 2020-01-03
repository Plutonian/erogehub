package com.goexp.galgame.data.source.getchu.query

import com.goexp.common.db.mongo.DBQuery
import com.goexp.galgame.common.db.mongo.query.GuideCreator
import com.goexp.galgame.common.model.game.guide.GameGuide
import com.goexp.galgame.data.source.getchu.DB_NAME

object GuideQuery {
  private val tlp = DBQuery[GameGuide](DB_NAME, "guide", GuideCreator).build

  def apply() = tlp

}