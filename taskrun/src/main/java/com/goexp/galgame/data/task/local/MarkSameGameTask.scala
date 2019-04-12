package com.goexp.galgame.data.task.local

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy, Piplline}
import com.goexp.galgame.data.piplline.handler.{DefaultMessageHandler, DefaultStarter}
import com.goexp.galgame.data.task.handler.MesType
import com.goexp.galgame.data.task.local.Fun.{FromAllBrand, ProcessBrandGame, UpdateState}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.io.Source

object MarkSameGameTask {

  val UPDATE_STATE = 8

  def main(args: Array[String]): Unit = new Piplline(new FromAllBrand)
    .registryCPUTypeMessageHandler(MesType.Brand, new ProcessBrandGame)
    .registryIOTypeMessageHandler(UPDATE_STATE, new UpdateState)
    .start()
}

package Fun {

  import com.goexp.galgame.data.task.local.MarkSameGameTask.UPDATE_STATE

  class FromAllBrand extends DefaultStarter[Integer] {
    override def process(msgQueue: MessageQueueProxy[Message[_]]): Unit = {
      //            msgQueue.offer(new Message<>(MesType.Brand, 10143));
      BrandQuery.tlp.query.list
        .forEach(brand => {
          msgQueue.offer(new Message[Integer](MesType.Brand, brand.id))

        })
    }
  }

  class ProcessBrandGame extends DefaultMessageHandler[Integer] {
    private val samelist = Source.fromInputStream(classOf[ProcessBrandGame].getResourceAsStream("/same.list")).getLines().toList
    private val packagelist = Source.fromInputStream(classOf[ProcessBrandGame].getResourceAsStream("/package.list")).getLines().toList


    private val logger = LoggerFactory.getLogger(classOf[ProcessBrandGame])

    override def process(message: Message[Integer], msgQueue: MessageQueueProxy[Message[_]]): Unit = {
      val brandId = message.entity
      logger.debug("<Brand> {}", brandId)

      GameQuery.fullTlp.query
        .where(Filters.eq("brandId", brandId))
        .list.asScala
        .groupBy(game => {
          if (isSameGame(game)) "same" else if (isPackageGame(game)) "package" else "other"
        })
        .flatMap({
          case ("same", value) =>
            value.toStream
              .filter(_.state eq GameState.UNCHECKED)
              .map(game => {
                game.state = GameState.SAME
                game
              })
          case ("package", value) =>
            value.toStream
              .filter(_.state eq GameState.UNCHECKED)
              .map({ game =>
                game.state = GameState.PACKAGE
                game
              })
          case ("other", value) =>
            value.toStream
              .filter(_.publishDate != null)
              .groupBy(_.publishDate)
              .values
              .filter(_.size > 1)
              .flatMap(games =>
                //games by date

                games.sortBy(_.name.length).drop(1).filter(_.state eq GameState.UNCHECKED).map({ game =>
                  game.state = GameState.SAME
                  game
                })
              )
          case _ =>
            throw new RuntimeException("Error")
        })
        .foreach(game => {
          logger.info(s"ID:${game.id} Name: ${game.name}  State: ${game.state}")
          msgQueue.offer(new Message[Game](UPDATE_STATE, game))
        })
    }

    private def isSameGame = (game: Game) => samelist.exists(str => game.name.contains(str))

    private def isPackageGame = (game: Game) => Option(game.`type`).map(_.asScala).getOrElse(List.empty).contains("セット商品") || packagelist.exists(str => game.name.contains(str))
  }

  class UpdateState extends DefaultMessageHandler[Game] {
    final private val logger = LoggerFactory.getLogger(classOf[UpdateState])
    final private val stateDB = new GameDB.StateDB

    override def process(message: Message[Game], msgQueue: MessageQueueProxy[Message[_]]): Unit = {
      val game = message.entity
      logger.debug("<Game> {}", game.id)
      stateDB.update(game)
    }
  }

}