package com.goexp.galgame.data.task.others.guide

import java.io.IOException
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString

import com.goexp.common.util.WebUtil.httpClient
import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.common.model.CommonGame.Guide.DataFrom
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.db.query.mongdb.GuideQuery
import com.goexp.galgame.data.parser.GameGuideParser
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy, Piplline}
import com.goexp.galgame.data.piplline.handler.DefaultStarter
import com.goexp.galgame.data.task.others.guide.UpdateGameGuideTask.CHARSET
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object Seiya_saiga_com {
  def main(args: Array[String]): Unit = {
    Network.initProxy()
    new Piplline(new Starter)
      .registryIOTypeMessageHandler(1, new PageContentHandler)
      .start()
  }

  private class Starter extends DefaultStarter[CommonGame.Guide] {
    private val logger = LoggerFactory.getLogger(classOf[Starter])

    override def process(msgQueue: MessageQueueProxy[Message[_]]): Unit = {
      val localList = GuideQuery.tlp.query
        .where(Filters.eq("from", DataFrom.seiya_saiga_com.getValue))
        .list.asScala.toSet

      logger.info(s"Local:${localList.size}")

      val req = HttpRequest.newBuilder.uri(URI.create("http://seiya-saiga.com/game/kouryaku.html")).build
      try {
        val html = httpClient.send(req, ofString(CHARSET)).body
        val remoteList = new GameGuideParser.Seiya_saiga().parse(html).asScala.toSet
        logger.info(s"Remote:${remoteList.size}")

        val insertlist = remoteList -- localList
        logger.info(s"Insert:${insertlist.size}")

        insertlist.foreach(guide => {
          msgQueue.offer(new Message[CommonGame.Guide](1, guide))
        })
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
      }
    }
  }

}
