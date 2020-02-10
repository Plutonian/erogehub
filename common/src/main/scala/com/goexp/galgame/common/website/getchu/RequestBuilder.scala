package com.goexp.galgame.common.website.getchu

import java.net.http.HttpRequest

import com.goexp.common.util.web.url._


object RequestBuilder {
  def create(url: String) = new RequestBuilder(url)

  def apply(url: String): RequestBuilder = new RequestBuilder(url)
}

class RequestBuilder private(val url: String) {


  private var builder = HttpRequest.newBuilder.uri(url)

  def adaltFlag: RequestBuilder = {
    builder = builder.header("Cookie", "getchu_adalt_flag=getchu.com")
    this
  }

  private def webClientParam = {
    builder = builder
      .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .header("Accept-Encoding", "gzip, deflate")
      .header("Accept-Language", "ja,en-US;q=0.7,en;q=0.3")
      .header("Cache-Control", "max-age=0")
      .header("Upgrade-Insecure-Requests", "1")
      .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
    this
  }

  def build: HttpRequest = {
    webClientParam
    builder.GET.build
  }
}

