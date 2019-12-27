package com.goexp.galgame.data.script.guide

import java.io.IOException
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString

import com.goexp.common.util.web.HttpUtil.noneProxyHttpClient
import com.goexp.common.util.web.url._
import com.goexp.galgame.common.model.game.guide.DataFrom
import com.goexp.galgame.data.GameGuideParser
import com.goexp.galgame.data.source.getchu.importor.GuideDB
import com.goexp.galgame.data.source.getchu.query.GuideQuery
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

object Sagaoz_net {

  private val logger = Logger(Sagaoz_net.getClass)

  def main(args: Array[String]): Unit = {
    val locals = GuideQuery.tlp
      .where(Filters.eq("from", DataFrom.sagaoz_net.value))
      .set().asScala

    logger.info(s"Local:${locals.size}")

    val request = HttpRequest.newBuilder.uri(DataFrom.sagaoz_net.href).build
    try {
      val html = noneProxyHttpClient.send(request, ofString(CHARSET)).body

      val remotes = new GameGuideParser.Sagaoz_Net().parse(html).toSet

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
