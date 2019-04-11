package com.goexp.galgame.data.task.local

import com.goexp.common.util.Strings
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.common.model.CommonGame.GameCharacter
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.{CVQuery, GameQuery}
import com.mongodb.client.model.Filters.{not, eq => same}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._


object GetTrueCVTask {
  private val gameDB = new GameDB

  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(GetTrueCVTask.getClass)

    val cvMap = CV.getMap(CVQuery.tlp.query.list).asScala

    logger.info("Init OK")

    val games = GameQuery.fullTlpWithChar.query.where(not(same("gamechar", null))).list.asScala

    games.par
      .filter(g => Option(g.gameCharacters).map(_.size).getOrElse(0) > 0)
      .map(game => {
        var change = false

        def isTargetGameCharacters(gChar: GameCharacter) = Strings.isNotEmpty(gChar.cv) && Strings.isEmpty(gChar.trueCV)

        game.gameCharacters =
          game.gameCharacters.asScala
            .map(gChar => {
              if (isTargetGameCharacters(gChar))
                cvMap.get(gChar.cv.trim.toLowerCase) match {
                  case Some(cv) => {
                    gChar.trueCV = cv.name

                    logger.info(s"CV:${gChar.cv},trueCV:${gChar.trueCV}  Game: ${game.name} ")
                    change = true
                  }
                  case _ => {}
                }
              gChar
            }).asJava

        (change, game)
      })
      .foreach({
        case (true, game) => {
          gameDB.updateChar(game)
        }
        case _ => {}
      })
  }
}