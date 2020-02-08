package com.goexp.galgame.data.source.getchu.task

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
import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object Util {
  private val logger = Logger(Util.getClass)

  def getGameAllImgs(g: Game) = {
    val list = mutable.ListBuffer[(Path, String)]()

    if (!g.smallImg.contains("now")) {


      /*
    Basic Info
     */
      val localNormal = Config.IMG_PATH.resolve(s"${GetchuGameLocal.normalImg(g)}.jpg")
      val remoteNormal = GetchuGameRemote.normalImg(g.id)
      logger.debug(s"Local:$localNormal(${Files.exists(localNormal)}) --> Remote:$remoteNormal")

      list.addOne((localNormal, remoteNormal))

      val localTiny = Config.IMG_PATH.resolve(s"${GetchuGameLocal.tiny120Img(g)}.jpg")
      val remoteTiny = GetchuGameRemote.getUrlFromSrc(g.smallImg)
      logger.debug(s"Local:$localTiny(${Files.exists(localTiny)}) --> Remote:$remoteTiny")

      list.addOne((localTiny, remoteTiny))

      val local200Tiny = Config.IMG_PATH.resolve(s"${GetchuGameLocal.tiny200Img(g)}.jpg")
      val remote200Tiny = GetchuGameRemote.tiny200Img(g.id)
      logger.debug(s"Local:$local200Tiny(${Files.exists(local200Tiny)}) --> Remote:$remote200Tiny")

      list.addOne((local200Tiny, remote200Tiny))

      val localLarge = Config.IMG_PATH.resolve(s"${GetchuGameLocal.largeImg(g)}.jpg")
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
        val pLocal = Config.IMG_PATH.resolve(s"${GetchuGameLocal.gameChar(g, p.index)}.jpg")
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

        val smallSimpleLocal = Config.IMG_PATH.resolve(s"${GetchuGameLocal.smallSimpleImg(g, sampleImg.index)}.jpg")
        val smallSimpleRemote = GetchuGameRemote.smallSimpleImg(sampleImg.src)

        list.addOne((smallSimpleLocal, smallSimpleRemote))

        val largeSimpleLocal = Config.IMG_PATH.resolve(s"${GetchuGameLocal.largeSimpleImg(g, sampleImg.index)}.jpg")
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

    logger.info(s"Need download:${hopeDownload}")

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
              logger.debug("", ex)

              ex.getCause match {
                case _: HttpTimeoutException =>
                  logger.warn(s"RequestTimeout")
                case _: ConnectException =>
                  logger.warn(s"CannotConnect")
                case e: IOException =>
                  logger.warn(s"ConnectionReset")
                case ErrorCodeException(errorCode) =>
                  logger.warn(s"Response Error:code=${errorCode}")
                case _: FileIsNotImageException =>
                  logger.warn(s"Not Image")
                case e =>
                  logger.error(s"NoneCatchExecption", e)
              }
              allDownLatch.countDown()
              null

            case _ =>
              logger.error(s"NoneCatchExecption")
              allDownLatch.countDown()
              null
          }
    }

    allDownLatch.await()

    logger.info("Download complete")
  }
}
