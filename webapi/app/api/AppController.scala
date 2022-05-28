package api

import com.goexp.galgame.common.model.DateType
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

    ok(Json.toJson(list)).as("application/json; charset=utf-8")
  }

}
