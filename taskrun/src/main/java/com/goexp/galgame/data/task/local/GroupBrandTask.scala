package com.goexp.galgame.data.task.local

import java.util

import com.goexp.common.util.Strings
import com.goexp.galgame.data.db.importor.mongdb.BrandDB
import com.goexp.galgame.data.db.query.mongdb.BrandQuery
import com.goexp.galgame.data.task.local.GroupBrandTask.Processor
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object GroupBrandTask {

  object Processor {
    private val hostRegex = "http[s]?://(?:ww[^\\.]+\\.)?(?<host>[^/]+)[/]?".r

    private val rem = util.Set.of("x", "ad", "bz", "cc", "co", "ea", "gr", "id", "in", "jp", "kt", "la", "me", "ne", "nu", "nz", "or", "oz", "ph", "pw", "sc", "tk", "to", "tv", "vc", "com", "net", "app", "ass", "fc2", "web", "jpn", "biz", "dti", //            "ssw",
      //            "q-x",
      "ics", "kir", //            "mmv",
      "org", "xii", //            "m3e",
      //            "zoo",
      //            "suki",
      "info", "from", "site", "soft", "sexy", "game", "software")

    private def clean(host: String) = host.split("\\.").toStream.filter((s: String) => !rem.contains(s)).toList

    private def getComp(host: String) = {
      clean(host).lastOption.getOrElse("")
    }

    def getHost(url: String) = {
      hostRegex.findFirstMatchIn(url).map(m => m.group("host")).getOrElse("")
    }

    def comp(url: String) = {
      val host = getHost(url)
      getComp(host)
    }
  }

  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(GroupBrandTask.getClass)
    val importor = new BrandDB

    BrandQuery.tlp.query.list.asScala.toStream
      .filter(b => Strings.isNotEmpty(b.website))
      .groupBy(b => Processor.comp(b.website))
      .filter({ case (k: String, v) => !k.isEmpty && v.size > 1 })
      .foreach({ case (k: String, v) =>
        //        logger.info(s"K:${k} V:$v ")

        v.foreach(b => {
          if (Strings.isEmpty(b.comp)) {
            logger.info(s"Raw:${b.comp} New:$k")
            b.comp = k
            importor.updateComp(b)
          }
        })
      })
  }


}

object GetRemove {
  def main(args: Array[String]): Unit =

    BrandQuery.tlp.query.list.asScala.toStream
      .filter(b => Strings.isNotEmpty(b.website))
      .flatMap(b => Processor.getHost(b.website).split("\\.").toStream.drop(1))
      .filter(s => !s.isEmpty)
      .distinct
      .sortBy(k => k.length)
      .foreach(k => println(s"'$k',"))
}