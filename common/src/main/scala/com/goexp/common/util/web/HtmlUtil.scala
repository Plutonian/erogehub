package com.goexp.common.util.web

import java.io.ByteArrayOutputStream

import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Document

object HtmlUtil {
  @throws[Exception]
  def doc2String(doc: Document): String = {
    val bos = new ByteArrayOutputStream
    TransformerFactory.newInstance.newTransformer
      .transform(new DOMSource(doc), new StreamResult(bos))
    bos.toString
  }
}
