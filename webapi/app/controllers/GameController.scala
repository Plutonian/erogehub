package controllers

import com.fasterxml.jackson.databind.DeserializationFeature._
import com.fasterxml.jackson.databind.ObjectMapper
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.mongodb.client.model.Filters
import play.libs.Json
import play.mvc.Controller
import play.mvc.Results.{notFound, ok}

class GameController extends Controller {


  val mapper = new ObjectMapper()
    .disable(FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
    .disable(ADJUST_DATES_TO_CONTEXT_TIME_ZONE)


  // Needs to set to Json helper
  Json.setObjectMapper(mapper)

  def info(id: Int) = {
    GameFullQuery().where(Filters.eq(id)).one() match {
      case Some(g) =>

        println(g)

        ok(Json.toJson(g)).as("application/json; charset=utf-8")
      case None => notFound()
    }
  }


}

