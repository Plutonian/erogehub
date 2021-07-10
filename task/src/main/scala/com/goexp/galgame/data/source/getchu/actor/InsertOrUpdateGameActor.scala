package com.goexp.galgame.data.source.getchu.actor

import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.piplline.handler.DefaultActor

import scala.io.{Codec, Source}
import scala.jdk.CollectionConverters._


/**
 * Check game is new or already has
 */
class InsertOrUpdateGameActor extends DefaultActor {

  override def receive = {
    case game: Game =>

      //already has
      if (GameDB.exist(game.id)) {
        logger.debug(s"<UpdateSmallImg> [${game.id}] ${game.smallImg}")
        GameDB.updateSmallImg(game)
      }
      else {
        //new game
        logger.debug(s"<Insert> Placehold: ${game.id}")
        GameDB.insert(game)
      }
      sendTo[DownloadPageActor](game.id)

  }

}

object InsertOrUpdateGameActor {
  private val samelist = {
    val source = Source.fromInputStream(getClass.getResourceAsStream("/same.list"))(Codec.UTF8)
    try source.getLines().toList finally source.close()
  }

  private val packagelist = {
    val source = Source.fromInputStream(getClass.getResourceAsStream("/package.list"))(Codec.UTF8)
    try source.getLines().toList finally source.close()
  }

  def isSameGame = (game: Game) => samelist.exists(str => game.name.contains(str))

  def isPackageGame = (game: Game) =>
    Option(game.`type`).map(_.asScala).getOrElse(List.empty).contains("セット商品") ||
      packagelist.exists(str => game.name.contains(str))
}