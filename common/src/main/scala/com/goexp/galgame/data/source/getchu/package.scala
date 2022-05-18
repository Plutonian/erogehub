package com.goexp.galgame.data.source

import java.nio.charset.Charset

package object getchu {
  val DB_NAME = "galgame"
  implicit val DEFAULT_CHARSET = Charset.forName("EUC-JP")
}
