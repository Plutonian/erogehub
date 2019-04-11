package com.goexp.galgame.data.task.others

import com.goexp.galgame.common.util.Network
import com.goexp.galgame.common.website.GetchuURL
import com.goexp.galgame.data.db.importor.mongdb.TagDB
import com.goexp.galgame.data.parser.GetchuTagParser
import com.goexp.galgame.data.task.client.GetChu
import org.slf4j.LoggerFactory

object ImportOnceTagTask {
  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(ImportOnceTagTask.getClass)

    Network.initProxy()

    /**
      * download page from getchu
      */
    val request = GetchuURL.RequestBuilder.create("http://www.getchu.com/pc/genre.html").adaltFlag.build
    val html = GetChu.getHtml(request)

    /**
      * parse html
      */

    val remoteTagList = new GetchuTagParser().parse(html)
    logger.info(s"Remote:${remoteTagList.size}")

    /**
      * save to db
      */
    new TagDB insert remoteTagList
  }
}