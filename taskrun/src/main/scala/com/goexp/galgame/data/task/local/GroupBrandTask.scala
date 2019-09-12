package com.goexp.galgame.data.task.local

import com.goexp.common.util.string.Strings
import com.goexp.galgame.data.db.importor.mongdb.BrandDB
import com.goexp.galgame.data.db.query.mongdb.BrandQuery
import com.goexp.galgame.data.task.local.GroupBrandTask.Extracker.getHost
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

object GroupBrandTask extends App {

  val logger = LoggerFactory.getLogger(GroupBrandTask.getClass)

  BrandQuery.tlp.query.list.asScala.to(LazyList)
    .filter(b => Strings.isNotEmpty(b.website))
    .groupBy(b => Extracker.getComp(b.website))
    .filter({ case (comp, v) => !comp.isEmpty && v.size > 1 })
    .foreach({ case (comp, v) =>
      v.foreach(b => {
        if (Strings.isEmpty(b.comp)) {
          logger.info(s"Raw:${b.comp} New:$comp")
          b.comp = comp
          BrandDB.updateComp(b)
        }
      })
    })


  object Extracker {
    private lazy val hostRegex = "http[s]?://(?:ww[^\\.]+\\.)?(?<host>[^/]+)[/]?".r

    private lazy val rem = Set("x", "ad", "bz", "cc", "co", "ea", "gr", "id", "in", "jp", "kt", "la", "me", "ne", "nu", "nz", "or", "oz", "ph", "pw", "sc", "tk", "to", "tv", "vc", "com", "net", "app", "ass", "fc2", "web", "jpn", "biz", "dti", //            "ssw",
      //            "q-x",
      "ics", "kir", //            "mmv",
      "org", "xii", //            "m3e",
      //            "zoo",
      //            "suki",
      "info", "from", "site", "soft", "sexy", "game", "software")

    def getComp(url: String) = {

      def clean(host: String) = host.split("\\.").to(LazyList).filter(!rem.contains(_))

      val host = getHost(url)
      clean(host).lastOption.getOrElse("")
    }

    def getHost(url: String) = {
      hostRegex.findFirstMatchIn(url).map(_.group("host")).getOrElse("")
    }
  }


}

object GetRemove {
  def main(args: Array[String]) =

    BrandQuery.tlp.query.list.asScala.to(LazyList)
      .filter(b => Strings.isNotEmpty(b.website))
      .flatMap(b => getHost(b.website).split(raw"\.").to(LazyList).drop(1))
      .filter(!_.isEmpty)
      .distinct
      .sortBy(_.length)
      .foreach(k => println(s"'$k',"))
}