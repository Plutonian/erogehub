package com.goexp.common.util

import java.io.{ByteArrayInputStream, IOException}
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.Objects
import java.util.zip.GZIPInputStream

object Gzip {

  implicit class GzipSupport(compressedBytes: Array[Byte]) {
    def unGzip() = {
      Gzip.unGzip(compressedBytes)
    }
  }

  private def unGzip(compressedBytes: Array[Byte]): Array[Byte] = {
    val s = new GZIPInputStream(new ByteArrayInputStream(compressedBytes))
    try return s.readAllBytes
    catch {
      case e: IOException =>
        e.printStackTrace()
    } finally if (s != null) s.close()
    null
  }

  def decode(compressedBytes: Array[Byte], charset: Charset): String = {
    Objects.requireNonNull(compressedBytes)
    Objects.requireNonNull(charset)
    val bytes = unGzip(compressedBytes)
    Objects.requireNonNull(bytes, "Unzip error")
    charset.decode(ByteBuffer.wrap(bytes)).toString
  }
}