package com.goexp.galgame.data.script.local

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.galgame.data.source.getchu.query.{CVQuery, GameFullWithCharQuery}
import com.mongodb.client.model.Filters.{not, eq => same}
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._


object GetTrueCVTask {
  private val logger = Logger(GetTrueCVTask.getClass)

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

    val list = CVQuery().scalaList().toList
    val localCV = getMap(list)

    logger.info("Init OK")

    GameFullWithCharQuery()
      .where(not(same("gamechar", null)))
      .scalaList().to(LazyList)
      .filter(g => Option(g.gameCharacters).map(_.size).getOrElse(0) > 0)
      .map(g => {

        var change = false

        g.gameCharacters = g.gameCharacters.asScala
          .map(p => {
            var tempP = p

            def isTarget(p: Person) = Strings.isNotEmpty(p.cv) && Strings.isEmpty(p.trueCV)

            val cv = Option(tempP.cv).map(_.trim.toLowerCase).getOrElse("")
            if (isTarget(tempP) && localCV.contains(cv)) {
              val trueCV = localCV(cv)
              tempP = tempP.copy(trueCV = trueCV.name)

              logger.info(s"${tempP.cv} --> ${tempP.trueCV}  Game: ${g.simpleView} ")
              change = true
            }

            tempP
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