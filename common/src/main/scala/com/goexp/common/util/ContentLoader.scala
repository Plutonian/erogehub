package com.goexp.common.util

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.file.{Files, Path}

object ContentLoader {
  def loadFromFile(path: Path, charset: Charset): String = {
    try {
      val bytes = Files.readAllBytes(path)
      val html = charset.decode(ByteBuffer.wrap(bytes)).toString
      return html
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
    null
  }
}