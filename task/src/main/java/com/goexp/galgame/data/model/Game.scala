package com.goexp.galgame.data.model

import java.util.Objects

import com.goexp.galgame.common.model.{CommonGame, GameState}

class Game extends CommonGame {
  var state: GameState = _
  var brandId = 0

  override def equals(o: Any): Boolean = {

    o match {
      case game: Game =>
        id == game.id &&
          //                Objects.equals(name, game.name) &&
          //                Objects.equals(publishDate, game.publishDate) &&
          //                Objects.equals(imgUrl, game.imgUrl) &&
          //                Objects.equals(website, game.website) &&
          Objects.equals(brandId, game.brandId) &&
          Objects.equals(writer, game.writer) &&
          Objects.equals(painter, game.painter) &&
          Objects.equals(`type`, game.`type`) &&
          Objects.equals(tag, game.tag) &&
          Objects.equals(story, game.story)
      case _ => false
    }
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