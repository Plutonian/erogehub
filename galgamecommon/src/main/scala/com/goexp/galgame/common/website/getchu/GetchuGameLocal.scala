package com.goexp.galgame.common.website.getchu

object GetchuGameLocal {

  def tiny120Img(gameId: Int) =
    s"${gameId}/game_t"

  def tiny200Img(gameId: Int) =
    s"${gameId}/game_t200"

  def normalImg(gameId: Int) =
    s"${gameId}/game_s"

  def largeImg(gameId: Int) =
    s"${gameId}/game_l"

  def largeSimpleImg(gameId: Int, index: Int) =
    s"${gameId}/simple_l_$index"

  def smallSimpleImg(gameId: Int, index: Int) = {
    s"${gameId}/simple_s_$index"
  }

  def gameChar(gameId: Int, index: Int) =
    s"${gameId}/char_s_$index"

}
