package com.goexp.galgame.data.task.handler

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.GameQuery
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.download.contentprovider.brand.LocalProvider
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.include
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._


class ProcessGameList extends DefaultMessageHandler[Int] {
  final private val logger = LoggerFactory.getLogger(classOf[ProcessGameList])
  final private val importor = new GameDB

  override def process(message: Message[Int], msgQueue: MessageQueueProxy[Message[_]]): Unit = {
    val brandId = message.entity
    logger.debug("<Brand> {}", brandId)
    try {
      val remoteGames = LocalProvider.getList(brandId).asScala
      val localGames = GameQuery.fullTlp.query.select(include("_id"))
        .where(Filters.eq("brandId", brandId))
        .list.asScala.toStream.map((game: Game) => game.id).toSet


      if (remoteGames.size != localGames.size) {
        logger.debug(s"Brand:$brandId,RemoteCount:${remoteGames.size},LocalCount:${localGames.size}")

        remoteGames.toStream
          .filter(g => !localGames.contains(g.id))
          //New Game
          .foreach(game => {

          game.brandId = brandId
          game.state = GameState.UNCHECKED
          logger.info("<Insert> {}", game.simpleView)
          importor.insert(game)

          msgQueue.offer(new Message[Int](MesType.NEED_DOWN_GAME, game.id))
        })
      }
    } catch {
      case e: Exception =>
        logger.error("Brand:{}", brandId)
        e.printStackTrace()
    }
  }
}