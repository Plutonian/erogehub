package api

import com.mongodb.client.model.Sorts.ascending
import play.libs.Json
import play.mvc.Controller
import play.mvc.Results.ok
import qurey.TagQuery

class TagController extends Controller {
  Config.init()

  def list() = {

    val list = TagQuery().sort(ascending("order")).list()

    ok(Json.toJson(list)).as("application/json; charset=utf-8")
  }

}
