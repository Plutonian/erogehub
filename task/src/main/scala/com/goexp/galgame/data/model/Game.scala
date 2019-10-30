package com.goexp.galgame.data.model

import java.util.{Objects, StringJoiner}

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.galgame.common.model.game.{CommonGame, GameState}

class Game extends CommonGame {
  var state: GameState = _
  var brandId = 0
  var group: String = _

  override def equals(o: Any): Boolean = {

    o match {
      case game: Game =>
        id == game.id &&
          //                Objects.equals(name, game.name) &&
          //                Objects.equals(publishDate, game.publishDate) &&
          //                Objects.equals(imgUrl, game.imgUrl) &&
          //                Objects.equals(website, game.website) &&
          (brandId == game.brandId) &&
          (writer == game.writer) &&
          (painter == game.painter) &&
          (`type` == game.`type`) &&
          (tag == game.tag) &&
          (story == game.story)
      case _ => false
    }
  }

  def simpleView: String = "Game{" + "id=" + RED.s(String.valueOf(id)) + ", name='" + RED.s(name) + '\'' + ", publishDate=" + publishDate + ", imgUrl='" + smallImg + '\'' + ", isNew='" + isNew + '\'' + '}'


  override def toString: String = {
    new StringJoiner(", ", classOf[Game].getSimpleName + "[", "]")
      .add("id=" + id)
      .add("name='" + RED.s(name) + "'")
      .add("publishDate=" + publishDate)
      .add("smallImg='" + smallImg + "'")
      .add("website='" + website + "'")
      .add("writer=" + writer)
      .add("painter=" + painter)
      .add("type=" + `type`)
      .add("tag=" + tag)
      .add("gameImgs=" + gameImgs)
      .add("gameCharacters=" + gameCharacters)
      //                .add("story='" + story + "'")
      //                .add("intro='" + intro + "'")
      .toString
  }

  override def hashCode: Int =
    Objects.hash(
      id.asInstanceOf,
      //                name,
      //                publishDate,
      //                imgUrl,
      brandId.asInstanceOf,
      writer,
      painter,
      `type`,
      tag,
      story)

}