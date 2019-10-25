package com.goexp.galgame.data.task.handler

import java.io.IOException
import java.net.ConnectException
import java.net.http.HttpTimeoutException
import java.nio.file.{Files, Path}
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{CompletionException, CountDownLatch}

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.util.ImageUtil
import com.goexp.galgame.common.util.ImageUtil.{ErrorCodeException, FileIsNotImageException}
import com.goexp.galgame.common.website.getchu.{GetchuGameLocal, GetchuGameRemote}
import com.goexp.galgame.data.model.Game
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object Util {
  private val logger = LoggerFactory.getLogger(Util.getClass)

  def getGameAllImgs(g: Game) = {
    val list = mutable.ListBuffer[(Path, String)]()

    if (!g.smallImg.contains("now")) {


      /*
    Basic Info
     */
      val localSmall = Config.IMG_PATH.resolve(s"${GetchuGameLocal.smallImg(g.id)}.jpg")
      val remoteSmall = GetchuGameRemote.smallImg(g.id)
      logger.debug(s"Local:$localSmall(${Files.exists(localSmall)}) --> Remote:$remoteSmall")

      list.addOne((localSmall, remoteSmall))

      val localTiny = Config.IMG_PATH.resolve(s"${GetchuGameLocal.tiny120Img(g.id)}.jpg")
      val remoteTiny = GetchuGameRemote.getUrlFromSrc(g.smallImg)
      logger.debug(s"Local:$localTiny(${Files.exists(localTiny)}) --> Remote:$remoteTiny")

      list.addOne((localTiny, remoteTiny))

      val local200Tiny = Config.IMG_PATH.resolve(s"${GetchuGameLocal.tiny200Img(g.id)}.jpg")
      val remote200Tiny = GetchuGameRemote.tiny200Img(g.id)
      logger.debug(s"Local:$local200Tiny(${Files.exists(local200Tiny)}) --> Remote:$remote200Tiny")

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

    list.to(LazyList).filter { case (local: Path, _) => !Files.exists(local) }
  }

  def downloadImage(games: LazyList[Game]) = {
    val imgs = games
      .flatMap { g =>
        getGameAllImgs(g)
      }

    var requestNum = 0
    val hopeDownload = imgs.size

    logger.info("Need download:{}", hopeDownload)

    val responseNum = new AtomicInteger(0)

    val allDownLatch = new CountDownLatch(hopeDownload)

    imgs.foreach {
      case (local: Path, remote: String) =>
        def rPath(path: Path) =
          path.iterator().asScala.to(LazyList).takeRight(2).mkString("/")


        val showLocal = rPath(local)

        requestNum += 1
        logger.info(s"Downloading... [$requestNum/$hopeDownload] ${showLocal} --> $remote")

        ImageUtil.loadFromAsyn(remote)
          .thenApply[Array[Byte]] { res => res.body() }
          .thenAccept { bytes =>
            Files.createDirectories(local.getParent)
            Files.write(local, bytes)

            logger.info(s"Success!!! [${responseNum.incrementAndGet()}/$hopeDownload] ${showLocal} (${bytes.length})")

            allDownLatch.countDown()

          }
          .exceptionally {
            case ex: CompletionException =>
              logger.debug("{}", ex)

              ex.getCause match {
                case _: HttpTimeoutException =>
                  logger.error(s"RequestTimeout")
                case _: ConnectException =>
                  logger.error(s"CannotConnect")
                case e: IOException =>
                  logger.error(s"ConnectionReset")
                  logger.debug("IOException", e)
                case ErrorCodeException(errorCode) =>
                  logger.error(s"Response Error:code={}", errorCode)
                case _: FileIsNotImageException =>
                  logger.error(s"Not Image")
                case e =>
                  logger.error(s"NoneCatchExecption")
                  e.printStackTrace()
              }
              allDownLatch.countDown()
              null

            case _ =>
              logger.error(s"NoneCatchExecption")
              allDownLatch.countDown()
              null
          }


      //        if (requestNum % batchCounts == 0) {
      //          TimeUnit.SECONDS.sleep(waitTime)
      //        }

      case _ =>
    }

    allDownLatch.await()

    logger.info("download Succ")
  }
}
