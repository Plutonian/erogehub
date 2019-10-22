package com.goexp.galgame.data.task.local

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.common.model.CommonGame.GameCharacter
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.{CVQuery, GameQuery}
import com.mongodb.client.model.Filters.{not, eq => same}
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._


object GetTrueCVTask {
  private val logger = LoggerFactory.getLogger(GetTrueCVTask.getClass)

  type Person = GameCharacter

  def getMap(cvList: List[CV]) =
    cvList.to(LazyList)
      .flatMap(cv => {
        cv.nameStr.split("[=＝]")
          .map((name: String) => {
            val c = new CV
            c.name = cv.name
            c.trueName = name.trim.toLowerCase
            c
          })
      })
      .map(cv => cv.trueName -> cv)
      .toMap


  def main(args: Array[String]): Unit = {

    val list = CVQuery.tlp.query.scalaList.toList
    val localCV = getMap(list)

    logger.info("Init OK")

    GameQuery.fullTlpWithChar.query
      .where(not(same("gamechar", null)))
      .scalaList.to(LazyList)
      .filter(g => Option(g.gameCharacters).map(_.size).getOrElse(0) > 0)
      .map(g => {

        var change = false

        g.gameCharacters = g.gameCharacters.asScala
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
          GameDB.updateChar(game)
        case _ =>
      }
  }


}