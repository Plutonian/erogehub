package api

import api.common.ExpendResult.ToJson
import com.mongodb.client.model.Filters.regex
import play.libs.Json
import play.mvc.Results.ok
import qurey.GuideQuery

class GuideController {


  api.Config.init()

  def search(searchKey: String) = {

    val list = GuideQuery()
      .where(regex("title", s"^$searchKey"))
      .list()

    ok(Json.toJson(list)).asJson()

  }
}
