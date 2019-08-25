package com.goexp.galgame.gui.db.mongo

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.common.db.mongo.DB_NAME

package object gamedb {
  val tlp = new DBOperatorTemplate(DB_NAME, "game")
}
