package com.goexp.galgame.data.task.local

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy, Piplline}
import com.goexp.galgame.data.piplline.handler.{DefaultMessageHandler, DefaultStarter}
import com.goexp.galgame.data.task.download.contentprovider.brand.LocalProvider
import com.goexp.galgame.data.task.handler.MesType
import com.goexp.galgame.data.task.handler.game.{Bytes2Html, Html2GameOK, LocalGameHandler, ProcessGameOK}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._


object ImportFromLocalAliveBrandTask {

  def main(args: Array[String]) = {
    Network.initProxy()

    new Piplline(new ImportFromLocalAliveBrandTask.StartFromAllAliveBrand)
      .registryCPUTypeMessageHandler(MesType.Brand, new ProcessGameList)
      .registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler)
      .registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html)
      .registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK)
      .registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK)
      .start()
  }

  class StartFromAllAliveBrand extends DefaultStarter[Int] {
    override def process(msgQueue: MessageQueueProxy[Message[_]]) = {
      BrandQuery.tlp.query.list
        .forEach(brand => {
          msgQueue.offer(new Message[Int](MesType.Brand, brand.id))
        })

      println("All Done!!!")
    }
  }

  class ProcessGameList extends DefaultMessageHandler[Int] {
    private val logger = LoggerFactory.getLogger(classOf[ProcessGameList])
    private val importor = new GameDB

    override def process(message: Message[Int], msgQueue: MessageQueueProxy[Message[_]]) = {
      val brandId = message.entity
      logger.debug("<Brand> {}", brandId)

      try {
        val remoteGames = LocalProvider.getList(brandId).toSet
        val localGames = GameQuery.fullTlp.query
          .where(Filters.eq("brandId", brandId))
          .set.asScala


        if (remoteGames.size > localGames.size) {
          logger.debug(s"Brand:$brandId,RemoteCount:${remoteGames.size},LocalCount:${localGames.size}")

          val insertGames = remoteGames -- localGames

          insertGames.foreach(game => {
            game.brandId = brandId
            game.state = GameState.UNCHECKED

            logger.info(s"<Insert> ${game.simpleView}")

            importor.insert(game)
            msgQueue.offer(new Message[Int](MesType.Game, game.id))
          })
        }
      } catch {
        case e: Exception =>
          logger.error("Brand:{}", brandId)
          e.printStackTrace()
      }
    }
  }

}