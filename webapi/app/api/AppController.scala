package api

import api.common.ExpendResult.ToJson
import com.goexp.galgame.common.model.{DateItem, DateType, Dates, Emotion}
import play.libs.Json
import play.mvc.Controller
import play.mvc.Results.ok

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._

case class Item(
                 @BeanProperty name: String,
                 @BeanProperty start: String,
                 @BeanProperty end: String
               )

class AppController extends Controller {
  api.Config.init()

  def dateCommand() = {

    val list = DateType.values().to(LazyList).map { dateType => Item(dateType.name, dateType.start.toString, dateType.end.toString) }.asJava

    ok(Json.toJson(list)).asJson()
  }

  def nearYears() = {

    val list = Dates.yearsIn5().to(LazyList).map { case DateItem(name, range) => Item(name, range.start.toString, range.end.toString) }.asJava
    ok(Json.toJson(list)).asJson()

  }

  def monthsOfThisYear() = {

    val list = Dates.monthOfThisYear().to(LazyList).map { case DateItem(name, range) => Item(name, range.start.toString, range.end.toString) }.asJava
    ok(Json.toJson(list)).asJson()
  }

  def emotions() = {
    val list = Emotion.EMOTIONS.to(LazyList).sortBy(_.value).reverse.asJava
    ok(Json.toJson(list)).asJson()
  }

}
