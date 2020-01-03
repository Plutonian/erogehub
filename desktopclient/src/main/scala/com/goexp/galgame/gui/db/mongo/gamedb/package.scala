package com.goexp.galgame.gui.db.mongo

import com.goexp.common.db.mongo.DBOperator

package object gamedb {
  val tlp = new DBOperator(DB_NAME, "game")
}
