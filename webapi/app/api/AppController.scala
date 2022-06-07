package api

import api.common.ExpendResult.ToJson
import com.goexp.galgame.common.model.{DateItem, Dates, Emotion}
import play.libs.Json
import play.mvc.Controller
import play.mvc.Results.ok

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._

case class Item(
                 @BeanProperty title: String,
                 @BeanProperty range: Range,
                 @BeanProperty index: Int
               )

case class Range(
                  @BeanProperty start: String,
                  @BeanProperty end: String
                )

class AppController extends Controller {
  api.Config.init()

  //  def dateCommand() = {
  //
  //    val list = DateType.values().to(LazyList).map { dateType => Item(dateType.name, Range(dateType.start.toString, dateType.end.toString),1) }.asJava
  //
  //    ok(Json.toJson(list)).asJson()
  //  }

  def nearYears() = {

    val list = Dates.yearsAfter2000().to(LazyList).map { case DateItem(name, index, range) => Item(name, Range(range.start.toString, range.end.toString), index) }.asJava
    ok(Json.toJson(list)).asJson()

  }

  def monthsOfThisYear() = {

    val list = Dates.monthOfThisYear().to(LazyList).map { case DateItem(name, index, range) => Item(name, Range(range.start.toString, range.end.toString), index) }.asJava
    ok(Json.toJson(list)).asJson()
  }

  def emotions() = {
    val list = Emotion.EMOTIONS.to(LazyList).sortBy(_.value).reverse.asJava
    ok(Json.toJson(list)).asJson()
  }

}
