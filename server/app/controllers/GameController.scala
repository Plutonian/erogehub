package controllers

import com.fasterxml.jackson.databind.DeserializationFeature._
import com.fasterxml.jackson.databind.ObjectMapper
import com.goexp.common.util.date.DateUtil
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.goexp.galgame.gui.Config
import com.mongodb.client.model.Filters
import org.apache.velocity.VelocityContext
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


        val root = new VelocityContext()

        root.put("IMG_REMOTE", Config.IMG_REMOTE)
        root.put("GetchuGameLocal", GetchuGameLocal)
        root.put("LOCAL", GameLocation.LOCAL)
        root.put("DateUtil", DateUtil)
        root.put("Strings", Strings)
        root.put("g", g)


        val str = VelocityTemplateConfig
          .tpl("/tpl/game/detail/index.html")
          .process(root)


        println(g)

        ok(str).as("text/html")
      case None => notFound()
    }


  }

  def query()={
//    Filters.and(
//      Filters.eq()
//    )

  }
}
