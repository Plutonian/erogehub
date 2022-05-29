package api

import com.goexp.galgame.gui.db.mongo.query.GuideQuery
import com.mongodb.client.model.Filters.regex
import play.libs.Json
import play.mvc.Results.ok

class GuideController {


  api.Config.init()

  def search(searchKey: String) = {

    val list = GuideQuery()
      .where(regex("title", s"^$searchKey"))
      .list()

    ok(Json.toJson(list)).as("application/json; charset=utf-8")

  }
}
