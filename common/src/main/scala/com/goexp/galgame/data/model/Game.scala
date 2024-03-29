package com.goexp.galgame.data.model

import com.goexp.common.util.Logger
import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.Emotion
import com.goexp.galgame.common.model.game.{CommonGame, GameLocation, PlayState}
import com.goexp.galgame.common.website.getchu.{GetchuGameLocal, GetchuGameRemote}
import com.goexp.galgame.data.Config

import java.nio.file.{Files, Path}
import java.util.StringJoiner
import scala.beans.BeanProperty
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class Game extends CommonGame with Logger {
  @BeanProperty
  var location: GameLocation = _
  @BeanProperty
  var star: Int = 0
  @BeanProperty
  var brandId = 0

  @BeanProperty
  var emotion: Emotion = _

  @BeanProperty
  var playState: PlayState = _

  @BeanProperty
  var brand: Brand = _

  @BeanProperty
  var isSame = false

  def simpleView: String = s"[${RED.s(id.toString)}] [$publishDate] [$playState] [$emotion]   ${RED.s(name)}"


  override def toString: String = {
    new StringJoiner(", ", classOf[Game].getSimpleName + "[", "]")
      .add("id=" + id)
      .add("name='" + RED.s(name) + "'")
      .add("publishDate=" + publishDate)
      .add("smallImg='" + smallImg + "'")
      .add("brand=" + brand)
      .add("playState=" + playState)
      .add("emotion=" + emotion)
      //      .add("type=" + `type`)
      //      .add("tag=" + tag)
      //      .add("gameImgs=" + gameImgs)
      //      .add("gameCharacters=" + gameCharacters)
      //                .add("story='" + story + "'")
      //                .add("intro='" + intro + "'")
      .toString
  }


  def allImgs = {

    val g = this
    val list = mutable.ListBuffer[(Path, String)]()

    if (!g.smallImg.contains("now")) {

      logger.debug(s"NOT NOW IMAGE [${g.id}] [${g.name}] [${g.publishDate}]")


      /*
    Basic Info
     */
      val localNormal = Config.IMG_LOCAL_ROOT.resolve(s"${GetchuGameLocal.normalImg(g)}.jpg")
      val remoteNormal = GetchuGameRemote.normalImg(g.id)
      logger.debug(s"[Normal] [${g.id}] [${g.name}] [${g.publishDate}] Local:$localNormal(${Files.exists(localNormal)}) --> Remote:$remoteNormal")

      list.addOne((localNormal, remoteNormal))

//      val localTiny = Config.IMG_LOCAL_ROOT.resolve(s"${GetchuGameLocal.tiny120Img(g)}.jpg")
//      val remoteTiny = GetchuGameRemote.getUrlFromSrc(g.smallImg)
//      logger.debug(s"[Tiny] [${g.id}] [${g.name}] [${g.publishDate}] Local:$localTiny(${Files.exists(localTiny)}) --> Remote:$remoteTiny")
//
//      list.addOne((localTiny, remoteTiny))
//
//      val local200Tiny = Config.IMG_LOCAL_ROOT.resolve(s"${GetchuGameLocal.tiny200Img(g)}.jpg")
//      val remote200Tiny = GetchuGameRemote.tiny200Img(g.id)
//      logger.debug(s"[200Tiny] [${g.id}] [${g.name}] [${g.publishDate}] Local:$local200Tiny(${Files.exists(local200Tiny)}) --> Remote:$remote200Tiny")
//
//      list.addOne((local200Tiny, remote200Tiny))

      val localLarge = Config.IMG_LOCAL_ROOT.resolve(s"${GetchuGameLocal.largeImg(g)}.jpg")
      val remoteLarge = GetchuGameRemote.largeImg(g.id)
      logger.debug(s"[Large] [${g.id}] [${g.name}] [${g.publishDate}] Local:$localLarge(${Files.exists(localLarge)}) --> Remote:$remoteLarge")

      list.addOne((localLarge, remoteLarge))
    }

    /*
  GameChar
   */
    if (g.gameCharacters != null)
      g.gameCharacters.asScala.to(LazyList)
        .filter { p => Strings.isNotEmpty(p.img) }
        .foreach { p =>
          val pLocal = Config.IMG_LOCAL_ROOT.resolve(s"${GetchuGameLocal.gameChar(g, p.index)}.jpg")
          val pRemote = GetchuGameRemote.getUrlFromSrc(p.img)

          logger.debug(s"[Char] [${g.id}] [${g.name}] [${g.publishDate}] [${p.name}]  Local:$pLocal(${Files.exists(pLocal)}) --> Remote:$pRemote")

          list.addOne((pLocal, pRemote))
        }

    /*
  SimpleImg
   */
    if (g.gameImgs != null)
      g.gameImgs.asScala.to(LazyList)
        .filter { sampleImg => Strings.isNotEmpty(sampleImg.src) }
        .foreach { sampleImg =>

          val smallSimpleLocal = Config.IMG_LOCAL_ROOT.resolve(s"${GetchuGameLocal.smallSimpleImg(g, sampleImg.index)}.jpg")
          val smallSimpleRemote = GetchuGameRemote.smallSimpleImg(sampleImg.src)

          list.addOne((smallSimpleLocal, smallSimpleRemote))

          val largeSimpleLocal = Config.IMG_LOCAL_ROOT.resolve(s"${GetchuGameLocal.largeSimpleImg(g, sampleImg.index)}.jpg")
          val largeSimpleRemote = GetchuGameRemote.largeSimpleImg(sampleImg.src)

          list.addOne((largeSimpleLocal, largeSimpleRemote))
        }

    list.to(LazyList).filter { case (local: Path, _) => !Files.exists(local) }
  }


}