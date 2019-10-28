package com.goexp.galgame.gui.db.mongo

import com.goexp.common.db.mongo.DBOperatorTemplate

package object gamedb {
  val tlp = new DBOperatorTemplate(DB_NAME, "game")
}
