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
  private val logger = LoggerFactory.getLogger(GetTrueCVTask.getClass)

  type Person = GameCharacter

  def main(args: Array[String]) = {

    val list = CVQuery.tlp.query.list
    val localCV = CV.getMap(list).asScala

    logger.info("Init OK")

    val games = GameQuery.fullTlpWithChar.query.where(not(same("gamechar", null))).list.asScala

    games.par
      .filter(g => Option(g.gameCharacters).map(_.size).getOrElse(0) > 0)
      .map(g => {
        var change = false

        g.gameCharacters =
          g.gameCharacters.asScala
            .map(p => {
              def isTarget(p: Person) = Strings.isNotEmpty(p.cv) && Strings.isEmpty(p.trueCV)

              val cv = p.cv.trim.toLowerCase
              if (isTarget(p) && localCV.contains(cv)) {
                val trueCV = localCV(cv)
                p.trueCV = trueCV.name

                logger.info(s"CV:${p.cv},trueCV:${p.trueCV}  Game: ${g.name} ")
                change = true
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