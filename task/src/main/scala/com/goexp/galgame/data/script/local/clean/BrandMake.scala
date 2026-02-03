package com.goexp.galgame.data.script.local.clean

import com.goexp.galgame.data.source.getchu.importor.GameDB
import com.goexp.galgame.data.source.getchu.query.{BrandQuery, GameSimpleQuery}
import com.mongodb.client.model.Filters

object BrandMake {


  def main(args: Array[String]): Unit = {

    val maybeGame = GameSimpleQuery()
      //      .where(Filters.eq(22))
      .scalaList()
      .foreach {
        g =>
          BrandQuery().where(Filters.eq(g.brandId)).one() match {
            case Some(b) =>

              GameDB.updateBrand(g, b)

              println("Succ")
            case None =>
          }
      }




    //    val brands = BrandQuery()
    //      //      .where(Filters.ne("type", BrandType.BLOCK.value)) // not blocked
    //      .scalaList()
    //
    //    brands
    //      .foreach(brand => {
    //
    //        val games = GameSimpleQuery()
    //          .where(and(
    //            Filters.eq("brandId", brand.id)
    //          ))
    //          .scalaList().to(LazyList)
    //
    //        logger.trace(s"${brand.id} ${brand.name} ${games.size}")
    //
    //        val tags = games
    //          .filter {
    //            _.tag != null
    //          }
    //          .flatMap {
    //            _.tag.asScala
    //          }
    //
    //          .filter { s => Strings.isNotEmpty(s) }
    //          .filter { s => !Set("ファンディスク", "アニメーション", "学園", "女子学生").contains(s) }
    //          .groupBy { s => s }.to(LazyList)
    //
    //          .map { case (k, v) => (k, v.size) }
    //          .sortBy { case (_, size) => size }.reverse
    //          .take(5) //top 5
    //          .map { case (k, _) => k }
    //
    //        val statistics = calStat(games)
    //
    //
    //        if (tags.nonEmpty)
    //          logger.trace(s"Brand:${brand.name} \tTags :${tags.mkString(",")}\n")
    //
    //        logger.trace(s"$statistics")
    //
    //        BrandDB.updateStatistics(brand, tags.asJava, statistics)
    //
    //      })

  }


}
