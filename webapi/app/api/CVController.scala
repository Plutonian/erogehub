package api

import api.common.ExpendResult.ToJson
import com.goexp.galgame.common.model.CV
import com.goexp.galgame.data.source.getchu.query.CVQuery
import play.libs.Json
import play.mvc.Controller
import play.mvc.Results.ok

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._

case class CVGroup(
                    @BeanProperty star: Int,
                    @BeanProperty list: java.util.List[CV]
                  )

class CVController extends Controller {
  Config.init()

  def list() = {

    val list = CVQuery().scalaList()
      .groupBy(_.star).to(LazyList)
      .sortBy { case (k, _) => k }.reverse
      .map { case (k, v) => CVGroup(k, v.asJava) }
      .asJava

    ok(Json.toJson(list)).asJson()
  }

}
