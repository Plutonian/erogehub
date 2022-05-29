package com.goexp.galgame.common.model.game

import com.goexp.galgame.common.model.game.CommonGame.Titles

import java.time.LocalDate
import java.util
import java.util.Date
import java.util.regex.Pattern
import scala.beans.BeanProperty

object CommonGame {
  private val NAME_SPLITER_REX = Pattern.compile("""[〜\-「]""")

  case class Titles(
                     @BeanProperty mainTitle: String,
                     @BeanProperty subTitle: String
                   )

}

abstract class CommonGame {
  def getTitles = {
    val tName = name.replaceAll("(?:マウスパッド付|”Re-order”〜?|BLUE Edition|WHITE Edition|プレミアムエディション|EDITION|祝！TVアニメ化記念|“男の子用”付|“女の子用”付|期間限定感謝ぱっく|感謝ぱっく|Liar-soft Selection \\d{2})", "").trim
      .replaceAll("(?:\\[[^]]+\\]$)|(?:＜[^＞]+＞)|(?:（[^）]+）$)", "")
      .replaceAll("[\\s　〜][^\\s〜　]+[版]", "")
      .replaceAll("(?:CD|DVD)(?:-ROM版)?", "").trim
      .replaceAll("(?:\\[[^]]+\\]$)|(?:＜[^＞]+＞)|(?:（[^）]+）$)", "").trim

    val matcher = CommonGame.NAME_SPLITER_REX.matcher(tName)
    val find = matcher.find

    val mainTitle = if (find) tName.substring(0, matcher.start) else tName
    val subTitle = if (find) tName.substring(matcher.start) else ""

    Titles(mainTitle.trim, subTitle.trim)
  }


  @BeanProperty var id: Int = 0
  @BeanProperty var name: String = ""
  @BeanProperty var publishDate: LocalDate = _

  def getDate(): Date = {

    import java.time.ZoneId
    //default time zone//default time zone

    val defaultZoneId = ZoneId.systemDefault

    //creating the instance of LocalDate using the day, month, year info
    //    val localDate = LocalDate.of(2016, 8, 19)

    //local date + atStartOfDay() + default time zone + toInstant() = Date
    //    val date =

    Option(publishDate).map { date => Date.from(date.atStartOfDay(defaultZoneId).toInstant) }.orNull
  }

  var smallImg: String = ""
  var website = ""
  var writer: util.List[String] = _ //シナリオ
  @BeanProperty var painter: util.List[String] = _ //原画
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