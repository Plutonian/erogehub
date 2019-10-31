package com.goexp.galgame.data.source.getchu.task.handler

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.piplline.core.{Message, MessageHandler}
import org.slf4j.LoggerFactory

import scala.io.{Codec, Source}
import scala.jdk.CollectionConverters._


/**
  * Check game is new or already has
  */
class PreProcessGame extends MessageHandler {
  final private val logger = LoggerFactory.getLogger(classOf[PreProcessGame])

  override def process(message: Message) = {

    message.entity match {
      case game: Game =>

        //already has
        if (GameDB.exist(game.id)) {
          logger.debug("<Update> {}", game.simpleView)
          GameDB.update(game)
        }
        else {
          //new game
          import PreProcessGame._

          //Mark game is spec
          if (isSameGame(game)) {
            game.state = GameState.SAME
          }
          else {
            game.state = GameState.UNCHECKED
          }

          logger.info("<Insert> {} {}", game.simpleView, game.state)
          GameDB.insert(game)
        }
        send(classOf[DownloadGameHandler], game.id)
    }
  }


}

object PreProcessGame {
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