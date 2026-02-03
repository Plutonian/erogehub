package api

import api.common.ExpendResult.ToJson
import com.mongodb.client.model.Sorts.ascending
import play.libs.Json
import play.mvc.Controller
import play.mvc.Results.ok
import qurey.TagWebQuery

class TagController extends Controller {
  Config.init()

  def list() = {

    val list = TagWebQuery().sort(ascending("order")).list()

    ok(Json.toJson(list)).asJson()
  }

}
