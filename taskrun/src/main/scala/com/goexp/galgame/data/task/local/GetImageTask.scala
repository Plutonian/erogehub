package com.goexp.galgame.data.task.local

import java.nio.file.{Files, Path}
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{CountDownLatch, TimeUnit}

import com.goexp.common.util.date.DateUtil
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.model.GameState
import com.goexp.galgame.common.util.{ImageUtil, Network}
import com.goexp.galgame.common.website.getchu.{GetchuGameLocal, GetchuGameRemote}
import com.goexp.galgame.data.db.query.mongdb.GameQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters._
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object GetImageTask {
  private val logger = LoggerFactory.getLogger(GetImageTask.getClass)

  def main(args: Array[String]): Unit = {

    Network.initProxy()

    val start = LocalDate.of(2019, 1, 1)
    val end = start.plusYears(1)

    val games = GameQuery.fullTlp.query
      .where(and(
        gte("publishDate", DateUtil.toDate(s"${start} 00:00:00")),
        lte("publishDate", DateUtil.toDate(s"${end} 23:59:59")),
        not(Filters.eq("state", GameState.BLOCK.value)),
        not(Filters.eq("state", GameState.SAME.value))
      ))
      .list.asScala


    val imgs = games.flatMap { g =>
      val list = mutable.ListBuffer[(Path, String)]()

      if (!g.smallImg.contains("now")) {


        /*

         */
        val localSmall = Config.IMG_PATH.resolve(s"${GetchuGameLocal.smallImg(g.id)}.jpg")
        val remoteSmall = GetchuGameRemote.smallImg(g.id)
        logger.debug(s"Local:$localSmall(${Files.exists(localSmall)}) --> Remote:$remoteSmall")

        list.addOne((localSmall, remoteSmall))

        val localTiny = Config.IMG_PATH.resolve(s"${GetchuGameLocal.tinyImg(g.id)}.jpg")
        val remoteTiny = GetchuGameRemote.getUrlFromSrc(g.smallImg)
        logger.debug(s"Local:$localTiny(${Files.exists(localTiny)}) --> Remote:$remoteTiny")

        list.addOne((localTiny, remoteTiny))

        val localLarge = Config.IMG_PATH.resolve(s"${GetchuGameLocal.largeImg(g.id)}.jpg")
        val remoteLarge = GetchuGameRemote.largeImg(g.id)
        logger.debug(s"Local:$localLarge(${Files.exists(localLarge)}) --> Remote:$remoteLarge")

        list.addOne((localLarge, remoteLarge))
      }

      /*
      GameChar
       */
      g.gameCharacters.asScala.to(LazyList)
        .filter { p => Strings.isNotEmpty(p.img) }
        .foreach { p =>
          val pLocal = Config.IMG_PATH.resolve(s"${GetchuGameLocal.gameChar(g.id, p.index)}.jpg")
          val pRemote = GetchuGameRemote.getUrlFromSrc(p.img)

          logger.debug(s"Local:$pLocal(${Files.exists(pLocal)}) --> Remote:$pRemote")

          list.addOne((pLocal, pRemote))
        }

      /*
      SimpleImg
       */
      g.gameImgs.asScala.to(LazyList)
        .filter { sampleImg => Strings.isNotEmpty(sampleImg.src) }
        .foreach { sampleImg =>

          val smallSimpleLocal = Config.IMG_PATH.resolve(s"${GetchuGameLocal.smallSimpleImg(g.id, sampleImg.index)}.jpg")
          val smallSimpleRemote = GetchuGameRemote.smallSimpleImg(sampleImg.src)

          list.addOne((smallSimpleLocal, smallSimpleRemote))

          val largeSimpleLocal = Config.IMG_PATH.resolve(s"${GetchuGameLocal.largeSimpleImg(g.id, sampleImg.index)}.jpg")
          val largeSimpleRemote = GetchuGameRemote.largeSimpleImg(sampleImg.src)

          list.addOne((largeSimpleLocal, largeSimpleRemote))
        }

      list

    }
      .filter { case (local: Path, _) => !Files.exists(local) }
    var count = 0
    val counts = imgs.size
    val atomicCount = new AtomicInteger(counts)

    logger.info("Need download:{}", counts)

    val latch = new CountDownLatch(counts)
    imgs.foreach {
      case (local: Path, remote: String) =>
        logger.info(s"Local:$local --> Remote:$remote")

        try {

          ImageUtil.loadFromAsyn(remote)
            .thenApply[Array[Byte]] { res => res.body() }
            //              .exceptionally(e=>)
            .thenAccept { bytes =>
              Files.createDirectories(local.getParent)
              Files.write(local, bytes)


              logger.info(s"OK with:${local} Left:${atomicCount.decrementAndGet()}")


              latch.countDown()

            }


          count += 1
          if (count % 10 == 0)
            TimeUnit.SECONDS.sleep(5)
        }
        catch {
          case e: Exception =>
            e.printStackTrace()
        }
      case _ =>
    }

    latch.await()
  }

}
