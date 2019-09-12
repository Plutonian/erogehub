package com.goexp.galgame.data.task.others.guide

import java.io.IOException
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString

import com.goexp.common.util.web.HttpUtil.httpClient
import com.goexp.common.util.web.url._
import com.goexp.galgame.common.model.CommonGame.Guide.DataFrom
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.db.query.mongdb.GuideQuery
import com.goexp.galgame.data.parser.GameGuideParser
import com.goexp.galgame.data.piplline.core.{Message, Pipeline, Starter}
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

object Seiya_saiga_com extends App {
  Network.initProxy()
  new Pipeline(new AStarter)
    .regForIOType(new PageContentHandler)
    .start()

  private class AStarter extends Starter {
    private val logger = LoggerFactory.getLogger(classOf[AStarter])

    override def process() = {
      val locals = GuideQuery.tlp.query
        .where(Filters.eq("from", DataFrom.seiya_saiga_com.value))
        .set.asScala

      logger.info(s"Local:${locals.size}")

      val req = HttpRequest.newBuilder.uri(DataFrom.seiya_saiga_com.href).build
      try {
        val html = httpClient.send(req, ofString(CHARSET)).body
        val remotes = new GameGuideParser.Seiya_saiga().parse(html).toSet
        logger.info(s"Remote:${remotes.size}")

        val insertlist = remotes -- locals
        logger.info(s"Insert:${insertlist.size}")

        insertlist.foreach(guide => {
          send(Message(classOf[PageContentHandler].hashCode(), guide))
        })
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
      }
    }
  }

}
