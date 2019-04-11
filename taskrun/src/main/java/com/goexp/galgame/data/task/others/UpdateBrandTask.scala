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
  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(UpdateBrandTask.getClass)
    Network.initProxy()

    val localMap = BrandQuery.tlp.query.list.asScala.toStream.map(b => b.id -> b).toMap
    logger.info(s"Local:${localMap.size}")

    val request = GetchuURL.RequestBuilder.create("http://www.getchu.com/all/brand.html?genre=pc_soft").adaltFlag.build
    val html = GetChu.getHtml(request)
    val remoteBrands = new GetchuBrandParser().parse(html).asScala
    logger.info(s"Remote: ${remoteBrands.size}")

    val brandDb = new BrandDB

    remoteBrands.foreach(remote => {
      localMap.get(remote.id) match {
        // find in local
        case Some(local) =>
          if (Strings.isEmpty(local.website) && Strings.isNotEmpty(remote.website)) {
            logger.info(s"Local: $local,Remote: $remote")
            brandDb.updateWebsite(remote)
          }
        case None =>
          logger.info(s"<Insert> $remote")
          brandDb.insert(remote)
      }

    })
  }
}