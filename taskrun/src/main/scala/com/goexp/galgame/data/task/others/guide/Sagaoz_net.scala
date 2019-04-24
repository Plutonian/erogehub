package com.goexp.galgame.data.task.others.guide

import java.io.IOException
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString

import com.goexp.common.util.WebUtil.noneProxyClient
import com.goexp.galgame.common.model.CommonGame
import com.goexp.galgame.common.model.CommonGame.Guide.DataFrom
import com.goexp.galgame.data.db.query.mongdb.GuideQuery
import com.goexp.galgame.data.parser.GameGuideParser
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy, Piplline}
import com.goexp.galgame.data.piplline.handler.DefaultStarter
import com.goexp.galgame.data.task.others.guide.UpdateGameGuideTask.CHARSET
import com.mongodb.client.model.Filters
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object Sagaoz_net {
  def main(args: Array[String]) =
    new Piplline(new Starter)
      .regForIOType(1, new PageContentHandler)
      .start()

  private class Starter extends DefaultStarter {
    private val logger = LoggerFactory.getLogger(classOf[Starter])

    override def process() = {
      val locals = GuideQuery.tlp.query
        .where(Filters.eq("from", DataFrom.sagaoz_net.getValue))
        .set.asScala

      logger.info(s"Local:${locals.size}")

      val req = HttpRequest.newBuilder.uri(URI.create("http://sagaoz.net/foolmaker/game.html")).build
      try {
        val html = noneProxyClient.send(req, ofString(CHARSET)).body

        val remotes = new GameGuideParser.Sagaoz_Net().parse(html).toSet

        logger.info(s"Remote:${remotes.size}")

        val insertlist = remotes -- locals


        logger.info(s"Insert:${insertlist.size}")
        insertlist.foreach(guide => {
          send(new Message[CommonGame.Guide](1, guide))
        })
      } catch {
        case e@(_: IOException | _: InterruptedException) =>
          e.printStackTrace()
      }
    }
  }

}
