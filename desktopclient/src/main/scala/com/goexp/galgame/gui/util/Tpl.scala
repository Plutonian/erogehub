package com.goexp.galgame.gui.util

import scala.collection.mutable
import scala.io.{Codec, Source}

case class Tpl(name: String, classLoader: Class[_]) {

  case class TplSegment(html: String) {
    def put(key: String, value: String): TplSegment = {
      TplSegment(html.replace(s"{$key}", value))
    }

    def get() = html
  }

  val tpl = Source.fromURL(classLoader.getResource(name).toURI.toURL)(Codec.UTF8)
    .foldLeft[mutable.StringBuilder](new mutable.StringBuilder()) { case (builder, s) => builder.append(s) }
    .toString()

  def put(key: String, value: String): TplSegment = {
    TplSegment(tpl).put(key, value)
  }

  def get() = tpl

}
