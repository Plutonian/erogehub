package com.goexp.galgame.data.task.others

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.util.Network
import com.goexp.galgame.data.db.importor.mongdb.BrandDB
import com.goexp.galgame.data.db.query.mongdb.BrandQuery
import com.goexp.galgame.data.task.client.GetChu.BrandService
import org.slf4j.LoggerFactory

object UpdateBrandTask {
  private val logger = LoggerFactory.getLogger(UpdateBrandTask.getClass)

  def main(args: Array[String]): Unit = {

    Network.initProxy()

    val localMap = BrandQuery.tlp.query
      .scalaList.to(LazyList)
      .map(b => b.id -> b)
      .toMap

    logger.info(s"Local:${localMap.size}")


    //::TODO zip 2 future
    val remotes = BrandService.all()
    logger.info(s"Remote: ${remotes.size}")

    remotes
      .foreach { remote =>
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
      }

  }
}