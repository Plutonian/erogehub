package com.goexp.galgame.data.task.local

import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.data.db.importor.mongdb.GameDB.StateDB
import com.goexp.galgame.data.db.query.mongdb.{BrandQuery, GameQuery}
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.piplline.core.{Message, MessageHandler, Piplline, Starter}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.io.{Codec, Source}
import scala.jdk.CollectionConverters._

object MarkSameGameTask {

  def main(args: Array[String]) =
    new Piplline(new FromAllBrand)
      .regForCPUType(new ProcessBrandGame)
      .regForIOType(new UpdateState)
      .start()

  class FromAllBrand extends Starter {
    override def process() = {
      BrandQuery.tlp.query.list
        .forEach(brand => {
          send(Message(classOf[ProcessBrandGame].hashCode(), brand.id))
        })
    }
  }

  class ProcessBrandGame extends MessageHandler {
    private lazy val samelist = {
      val source = Source.fromInputStream(classOf[ProcessBrandGame].getResourceAsStream("/same.list"))(Codec.UTF8)
      try source.getLines().toList finally source.close()
    }

    private lazy val packagelist = {
      val source = Source.fromInputStream(classOf[ProcessBrandGame].getResourceAsStream("/package.list"))(Codec.UTF8)
      try source.getLines().toList finally source.close()
    }

    private val logger = LoggerFactory.getLogger(classOf[ProcessBrandGame])

    override def process(message: Message) = {
      message.entity match {
        case brandId: Int =>
          logger.debug("<Brand> {}", brandId)

          GameQuery.fullTlp.query
            .where(Filters.eq("brandId", brandId))
            .list.asScala
            .groupBy(game => {
              if (isSameGame(game)) "same" else if (isPackageGame(game)) "package" else "other"
            })
            .flatMap({
              case ("same", value) =>
                value.to(LazyList)
                  .filter(_.state eq GameState.UNCHECKED)
                  .map(game => {
                    game.state = GameState.SAME
                    game
                  })
              case ("package", value) =>
                value.to(LazyList)
                  .filter(_.state eq GameState.UNCHECKED)
                  .map({ game =>
                    game.state = GameState.PACKAGE
                    game
                  })
              case ("other", value) =>
                value.to(LazyList)
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
              //              case _ =>
              //                throw new RuntimeException("Error")
            })
            .foreach(game => {
              logger.info(s"ID:${game.id} Name: ${game.name}  State: ${game.state}")
              send(Message(classOf[UpdateState].hashCode(), game))
            })
      }


    }

    private def isSameGame = (game: Game) => samelist.exists(str => game.name.contains(str))

    private def isPackageGame = (game: Game) =>
      Option(game.`type`).map(_.asScala).getOrElse(List.empty).contains("セット商品") ||
        packagelist.exists(str => game.name.contains(str))

  }

  class UpdateState extends MessageHandler {
    private val logger = LoggerFactory.getLogger(classOf[UpdateState])

    override def process(message: Message) = {
      message.entity match {
        case game: Game =>
          logger.debug("<Game> {}", game.id)
          StateDB.update(game)
      }

    }
  }

}
