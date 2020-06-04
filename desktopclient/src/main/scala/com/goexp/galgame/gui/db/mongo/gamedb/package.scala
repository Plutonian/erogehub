package com.goexp.galgame.gui.db.mongo

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config

package object gamedb {
  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "game")
}
