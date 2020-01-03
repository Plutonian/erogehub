package com.goexp.galgame.data.source.getchu.query

import com.goexp.common.db.mongo.DBQueryTemplate
import com.goexp.galgame.common.db.mongo.query.GuideCreator
import com.goexp.galgame.common.model.game.guide.GameGuide
import com.goexp.galgame.data.source.getchu.DB_NAME

object GuideQuery {
  private val tlp = DBQueryTemplate[GameGuide](DB_NAME, "guide", GuideCreator).build

  def apply() = tlp

}