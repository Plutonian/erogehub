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
      .map(g => {
        var change = false

        def isTargetGameCharacters(p: GameCharacter) = Strings.isNotEmpty(p.cv) && Strings.isEmpty(p.trueCV)

        g.gameCharacters =
          g.gameCharacters.asScala
            .map(p => {
              if (isTargetGameCharacters(p))
                cvMap.get(p.cv.trim.toLowerCase) match {
                  case Some(cv) =>
                    p.trueCV = cv.name

                    logger.info(s"CV:${p.cv},trueCV:${p.trueCV}  Game: ${g.name} ")
                    change = true
                  case _ =>
                }
              p
            }).asJava

        (change, g)
      })
      .foreach {
        case (true, game) =>
          gameDB.updateChar(game)
        case _ =>
      }
  }
}