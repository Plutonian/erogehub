package com.goexp.galgame.data.task.local

import java.util

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.common.model.CommonGame.GameCharacter
import com.goexp.galgame.data.db.importor.mongdb.GameDB
import com.goexp.galgame.data.db.query.mongdb.{CVQuery, GameQuery}
import com.goexp.galgame.data.task.ansyn.Pool._
import com.mongodb.client.model.Filters.{not, eq => same}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.jdk.CollectionConverters._


object GetTrueCVTask extends App {
  private val logger = LoggerFactory.getLogger(GetTrueCVTask.getClass)

  type Person = GameCharacter

  def getMap(cvList: util.List[CV]) =
    cvList.asScala.to(LazyList)
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


  val list = CVQuery.tlp.query.list
  val localCV = getMap(list)

  logger.info("Init OK")

  GameQuery.fullTlpWithChar.query
    .where(not(same("gamechar", null)))
    .list.asScala.to(LazyList)
    .filter(g => Option(g.gameCharacters).map(_.size).getOrElse(0) > 0)
    .foreach(g => {

      val f = Future {
        var change = false

        g.gameCharacters =
          g.gameCharacters.asScala
            .map(p => {
              //              def isTarget(p: Person) = Strings.isNotEmpty(p.cv)
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

      }(CPU_POOL)

      f.foreach {
        case (true, game) =>
          GameDB.updateChar(game)
        case _ =>
      }(IO_POOL)

      Await.result(f, 10.minutes)

    })

}