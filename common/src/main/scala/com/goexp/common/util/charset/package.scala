package com.goexp.common.util

import java.nio.ByteBuffer
import java.nio.charset.Charset

package object charset {
  implicit def formatCharset(charset: String) = Charset.forName(charset)

  implicit class CharsetSupport(val charset: String) {
    def toCharset() = {
      Charset.forName(charset)
    }
  }

  implicit class CharsetSupport2(val bytes: Array[Byte]) {
    def decode(charset: String) = {
      charset.decode(ByteBuffer.wrap(bytes)).toString
    }

    def decode(charset: Charset) = {
      charset.decode(ByteBuffer.wrap(bytes)).toString
    }
  }

}
