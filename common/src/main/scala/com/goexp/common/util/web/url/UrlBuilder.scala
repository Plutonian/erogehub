package com.goexp.common.util.web.url

import java.nio.charset.StandardCharsets.UTF_8

import scala.collection.mutable

object UrlBuilder {
  def apply(host: String): UrlBuilder = new UrlBuilder(host)
}

final class UrlBuilder private(val host: String) {
  private val params = mutable.Map[String, String]()

  def param(name: String, value: String): UrlBuilder = {
    params += name -> value
    this
  }

  def param(name: String, value: Int): UrlBuilder = {
    param(name, value.toString)
  }

  def build: String = {
    val queryString = params.to(LazyList)
      .map { case (k, v) => s"$k=${v.urlEncode(UTF_8)}" }
      .mkString("&")

    s"$host?$queryString"
  }
}