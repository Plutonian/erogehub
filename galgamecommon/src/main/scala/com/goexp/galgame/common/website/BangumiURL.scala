package com.goexp.galgame.common.website

object BangumiURL {
  private val searchUrl = "http://bgm.tv/subject_search"

  def fromTitle(title: String):String = s"$searchUrl/$title"
}