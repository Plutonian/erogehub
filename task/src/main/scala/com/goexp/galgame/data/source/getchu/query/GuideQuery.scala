package com.goexp.galgame.data.source.getchu.query

import com.goexp.common.db.mongo.DBQueryTemplate
import com.goexp.galgame.data.source.getchu.DB_NAME
import com.goexp.galgame.common.db.mongo.query.GuideCreator
import com.goexp.galgame.common.model.game.guide.GameGuide

object GuideQuery {
  val tlp = new DBQueryTemplate.Builder[GameGuide](DB_NAME, "guide", GuideCreator).build

}