package com.goexp.galgame.gui.db.mongo

import com.goexp.db.mongo.DBOperator

package object gamedb {
  val tlp = new DBOperator(DB_NAME, "game")
}
