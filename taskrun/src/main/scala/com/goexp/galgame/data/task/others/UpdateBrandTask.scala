package com.goexp.galgame.data.task.others

import com.goexp.common.util.Strings
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.common.website.GetchuURL
import com.goexp.galgame.data.db.importor.mongdb.BrandDB
import com.goexp.galgame.data.db.query.mongdb.BrandQuery
import com.goexp.galgame.data.parser.GetchuBrandParser
import com.goexp.galgame.data.task.client.GetChu
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object UpdateBrandTask {
  private val logger = LoggerFactory.getLogger(UpdateBrandTask.getClass)

  def main(args: Array[String]) = {

    Network.initProxy()

    val localMap = BrandQuery.tlp.query
      .list.asScala.toStream
      .map(b => b.id -> b)
      .toMap

    logger.info(s"Local:${localMap.size}")

    val request = GetchuURL.RequestBuilder.create("http://www.getchu.com/all/brand.html?genre=pc_soft").adaltFlag.build
    val html = GetChu.getHtml(request)

    val remotes = new GetchuBrandParser().parse(html).toSet
    logger.info(s"Remote: ${remotes.size}")

    remotes.foreach(remote => {
      localMap.get(remote.id) match {
        // find in local
        case Some(local) =>
          if (Strings.isEmpty(local.website) && Strings.isNotEmpty(remote.website)) {
            logger.info(s"Local: $local,Remote: $remote")
            BrandDB.updateWebsite(remote)
          }
        case None =>
          logger.info(s"<Insert> $remote")
          BrandDB.insert(remote)
      }

    })
  }
}