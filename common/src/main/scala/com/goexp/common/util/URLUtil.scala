package com.goexp.common.util

import java.io.ByteArrayOutputStream
import java.net.URI
import java.util.Objects

import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Document

object URLUtil {
  @throws[Exception]
  def doc2String(doc: Document): String = {
    val bos = new ByteArrayOutputStream
    TransformerFactory.newInstance.newTransformer
      .transform(new DOMSource(doc), new StreamResult(bos))
    bos.toString
  }

  def favUrl(url: String): String = {
    Objects.requireNonNull(url)
    val uri = URI.create(url)
    String.format("%s://%s/%s", uri.getScheme, uri.getHost, "favicon.ico")
  }
}