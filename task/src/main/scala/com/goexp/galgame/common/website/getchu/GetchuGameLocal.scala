package com.goexp.galgame.common.website.getchu

import com.goexp.galgame.common.model.game.CommonGame

object GetchuGameLocal {

  @deprecated
  def tiny120Img(gameId: Int) =
    s"${gameId}/game_t"

  @deprecated
  def tiny200Img(gameId: Int) =
    s"${gameId}/game_t200"

  @deprecated
  def normalImg(gameId: Int) =
    s"${gameId}/game_s"

  @deprecated
  def largeImg(gameId: Int) =
    s"${gameId}/game_l"

  @deprecated
  def largeSimpleImg(gameId: Int, index: Int) =
    s"${gameId}/simple_l_$index"

  @deprecated
  def smallSimpleImg(gameId: Int, index: Int) = {
    s"${gameId}/simple_s_$index"
  }

  @deprecated
  def gameChar(gameId: Int, index: Int) =
    s"${gameId}/char_s_$index"


  @inline
  def gamePathString(game: CommonGame) = {
    val (year, month) = Option(game.publishDate).map { d => (d.getYear, d.getMonthValue) }.getOrElse((0, 0))
    s"$year/$month/${game.id}"
  }

  def tiny120Img(game: CommonGame) = {
    s"${gamePathString(game)}/game_t"
  }

  def tiny200Img(game: CommonGame) = {
    s"${gamePathString(game)}/game_t200"
  }

  def normalImg(game: CommonGame) = {
    s"${gamePathString(game)}/game_s"
  }

  def largeImg(game: CommonGame) = {
    s"${gamePathString(game)}/game_l"
  }

  def largeSimpleImg(game: CommonGame, index: Int) = {
    s"${gamePathString(game)}/simple_l_$index"
  }

  def smallSimpleImg(game: CommonGame, index: Int) = {
    s"${gamePathString(game)}/simple_s_$index"

  }

  def gameChar(game: CommonGame, index: Int) = {
    s"${gamePathString(game)}/char_s_$index"
  }

  def gameCharB(game: CommonGame, index: Int) = {
    s"${gamePathString(game)}/char_s_$index"
  }

}
