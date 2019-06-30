package com.goexp.galgame.data.task.local

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.piplline.core.{Message, Piplline}
import com.goexp.galgame.data.piplline.handler.{DefaultMessageHandler, DefaultStarter}
import com.goexp.galgame.data.task.download.contentprovider.brand.LocalProvider
import com.goexp.galgame.data.task.handler.MesType
import com.goexp.galgame.data.task.handler.game.DefaultGameProcessGroup
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._


object ImportFromLocalAliveBrandTask {

  def main(args: Array[String]) = {
    Network.initProxy()

    new Piplline(new ImportFromLocalAliveBrandTask.StartFromAllAliveBrand)
      .regForCPUType(MesType.Brand, new ProcessGameList)
      .regGroup(DefaultGameProcessGroup)
      .start()
  }

  class StartFromAllAliveBrand extends DefaultStarter {
    override def process() = {
      BrandQuery.tlp.query.list
        .forEach(brand => {
          send(new Message[Int](MesType.Brand, brand.id))
        })

      println("All Done!!!")
    }
  }

  class ProcessGameList extends DefaultMessageHandler[Int] {
    private val logger = LoggerFactory.getLogger(classOf[ProcessGameList])

    override def process(message: Message[Int]) = {
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

            GameDB.insert(game)
            send(new Message[Int](MesType.Game, game.id))
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