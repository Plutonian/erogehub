package com.goexp.galgame.data.script.local

import com.goexp.common.util.string.Strings
import com.goexp.galgame.data.ansyn.Pool
import com.goexp.galgame.data.ansyn.Pool.CPU_POOL
import com.goexp.galgame.data.model.Brand
import GroupBrandTask.Extracker.getHost
import com.goexp.galgame.data.source.getchu.importor.BrandDB
import com.goexp.galgame.data.source.getchu.query.BrandQuery
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object GroupBrandTask {
  val logger = Logger(GroupBrandTask.getClass)


  def main(args: Array[String]): Unit = {

    val brands = BrandQuery().scalaList().to(LazyList)

    val futures = brands
      .filter(b => Strings.isNotEmpty(b.website))
      .groupBy(b => Extracker.getComp(b.website))
      .filter { case (comp, v) => comp.nonEmpty && v.size > 1 }
      .flatten {
        case (comp, v: LazyList[Brand]) =>
          v.filter { b => Strings.isEmpty(b.comp) }
            .map { b =>
              logger.info(s"Raw:${b.comp} New:$comp")

              b.comp = comp

              Future {
                BrandDB.updateComp(b)
              }(Pool.DB_POOL)
            }
      }


    Await.result(Future.sequence(futures), Duration.Inf)
  }


  object Extracker {
    private val hostRegex = """https?://(?:ww[^.]+\.)?(?<host>[^/]+)/?""".r

    private val rem = Set("x", "ad", "bz", "cc", "co", "ea", "gr", "id", "in", "jp", "kt", "la", "me", "ne", "nu", "nz", "or", "oz", "ph", "pw", "sc", "tk", "to", "tv", "vc", "com", "net", "app", "ass", "fc2", "web", "jpn", "biz", "dti", //            "ssw",
      //            "q-x",
      "ics", "kir", //            "mmv",
      "org", "xii", //            "m3e",
      //            "zoo",
      //            "suki",
      "info", "from", "site", "soft", "sexy", "game", "software")

    def getComp(url: String) = {

      def clean(host: String) = host.split("""\.""").to(LazyList).filter(!rem.contains(_))

      val host = getHost(url)
      clean(host).lastOption.getOrElse("")
    }

    def getHost(url: String) = {
      hostRegex.findFirstMatchIn(url).map(_.group("host")).getOrElse("")
    }
  }

}

object GetRemove extends App {

  BrandQuery().scalaList().to(LazyList)
    .filter(b => Strings.isNotEmpty(b.website))
    .flatMap(b => getHost(b.website).split("""\.""").to(LazyList).drop(1))
    .filter(!_.isEmpty)
    .distinct
    .sortBy(_.length)
    .foreach(k => println(s"'$k',"))
}