package com.goexp.galgame.data.script.source.getchu.others

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.common.website.getchu
import com.goexp.galgame.data.Client
import com.goexp.galgame.data.source.getchu.DEFAULT_CHARSET
import com.goexp.galgame.data.source.getchu.importor.TagDB
import com.goexp.galgame.data.source.getchu.parser.GetchuTagParser
import org.slf4j.LoggerFactory

object ImportOnceTagTask {
  def main(args: Array[String]) = {
    val logger = LoggerFactory.getLogger(ImportOnceTagTask.getClass)

    Network.initProxy()

    /**
      * download page from getchu
      */
    val request = getchu.RequestBuilder("http://www.getchu.com/pc/genre.html").adaltFlag.build
    val html = Client.getHtml(request)

    /**
      * parse html
      */

    val remotes = new GetchuTagParser().parse(html)
    logger.info(s"Remote:${remotes.size}")

    /**
      * save to db
      */
    TagDB insert remotes
  }
}