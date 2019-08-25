package com.goexp.common.util

import java.nio.charset.Charset

package object charset {
  implicit def formatCharset(charset: String) = Charset.forName(charset)
}
