package com.goexp.galgame.gui.db.mongo

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME

package object gamedb {
  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "game")
}
