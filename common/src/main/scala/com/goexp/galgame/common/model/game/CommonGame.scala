package com.goexp.galgame.common.model.game

import com.goexp.common.util.date.DateUtil
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.CommonGame.Titles

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util
import scala.beans.BeanProperty

object CommonGame {

  object Titles {
    private val titleMatcher = """^(?<mainTitle>.+)(?<subTitle>〜.+〜|[-‐−].+[-‐−])(?<other>.*)$""".r

    def apply(name: String): Titles = {
      name match {
        case titleMatcher(mainTitle, subTitle, other) =>
          val titles = Titles(mainTitle, subTitle)
          titles.other = other
          titles
        case _ => Titles(name, null)
      }
    }

  }

  case class Titles(
                     @BeanProperty mainTitle: String,
                     @BeanProperty subTitle: String

                   ) {
    @BeanProperty
    var other: String = _
  }

}

abstract class CommonGame {
  def getTitles = {

    if (Strings.isNotEmpty(this.mainTitle)) {
      Titles(this.mainTitle.trim, this.subTitle.trim)
    } else {
      val cleanedName = name
        .replaceAll("""(?:マウスパッド付|The アニメ|”Re-order”〜?|BLUE Edition|WHITE Edition|プレミアムエディション|EDITION|祝！TVアニメ化記念|“男の子用”付|“女の子用”付|期間限定感謝ぱっく|感謝ぱっく|Liar-soft Selection \d{2})""", "").trim
        .replaceAll("""(?:\[.+]|＜.+＞)$""", "").trim //replace  ＜XXXXXXXXXXXXXX＞ [xxxxxxxxxx]
        .replaceAll("""(?<=[）　) ])\S+版""", "").trim // remove XXXX版
        .replaceAll("""(?<=\s)\S+付き?$""", "").trim // remove XXXX版


      Titles(cleanedName)
    }

  }


  @BeanProperty var id: Int = 0
  @BeanProperty var name: String = ""
  @BeanProperty var publishDate: LocalDate = _

  var mainTitle: String = _
  var subTitle: String = _

  def getDateFormatString(): String = {
    if (publishDate != null)

      if (DateUtil.needFormat(publishDate)) {
        DateUtil.formatDate(publishDate)

      } else {
        publishDate.format(DateTimeFormatter.ofPattern("yyyy-MM"))
      }
    else
      null
  }

  def getDateString(): String = {

    Option(publishDate).map {
      _.toString
    }.orNull

  }

  def isPublished() = {
    Option(publishDate).exists(_.isBefore(LocalDate.now()))
  }


  var smallImg: String = ""
  var website = ""
  @BeanProperty
  var writer: util.List[String] = _ //シナリオ
  @BeanProperty
  var painter: util.List[String] = _ //原画
  var `type`: util.List[String] = _ //サブジャンル
  @BeanProperty var tag: util.List[String] = _
  @BeanProperty var story: String = "" // ストーリー(HTML)
  @BeanProperty var intro: String = ""
  @BeanProperty var gameCharacters: util.List[GameCharacter] = _
  @BeanProperty var gameImgs: util.List[GameImg] = _
  @BeanProperty var isNew = false
  //  @BeanProperty var isAdult = true


  def canEqual(other: Any): Boolean = other.isInstanceOf[CommonGame]

  override def equals(other: Any): Boolean = other match {
    case that: CommonGame =>
      (that canEqual this) &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    id
  }
}