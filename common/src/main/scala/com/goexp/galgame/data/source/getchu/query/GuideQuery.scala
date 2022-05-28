package com.goexp.galgame.data.source.getchu.query

import com.goexp.db.mongo.DBQuery
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.GuideCreator
import com.goexp.galgame.common.model.game.guide.GameGuide

object GuideQuery {
  private val tlp = DBQuery[GameGuide](Config.DB_STRING, DB_NAME, "guide", GuideCreator).build

  def apply() = tlp

}