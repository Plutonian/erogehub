package com.goexp.galgame.data.script.guide

import java.io.IOException
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString

import com.goexp.common.util.web.HttpUtil.httpClient
import com.goexp.common.util.web.url._
import com.goexp.galgame.common.model.game.guide.DataFrom
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.GameGuideParser
import com.goexp.galgame.data.source.getchu.importor.GuideDB
import com.goexp.galgame.data.source.getchu.query.GuideQuery
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

object Seiya_saiga_com {
  private val logger = Logger(Seiya_saiga_com.getClass)

  def main(args: Array[String]): Unit = {

    Network.initProxy()

    /*
    Local
     */
    val locals = GuideQuery.tlp
      .where(Filters.eq("from", DataFrom.seiya_saiga_com.value))
      .set().asScala

    logger.info(s"Local:${locals.size}")

    /*
    Remote
     */
    val request = HttpRequest.newBuilder.uri(DataFrom.seiya_saiga_com.href).build
    try {
      val html = httpClient.send(request, ofString(CHARSET)).body
      val remotes = new GameGuideParser.Seiya_saiga().parse(html).toSet
      logger.info(s"Remote:${remotes.size}")

      val insertlist = remotes -- locals
      logger.info(s"Insert:${insertlist.size}")

      insertlist.foreach { guide =>
        logger.info(s"insert:${guide.title}")
        GuideDB.insert(guide)
      }
    } catch {
      case e@(_: IOException | _: InterruptedException) =>
        e.printStackTrace()
    }

  }

}
