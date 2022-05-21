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
import org.bson.BsonDocument
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.{BsonValueCodecProvider, ValueCodecProvider}
import play.libs.Json
import play.mvc.Controller
import play.mvc.Http.Request
import play.mvc.Results.{notFound, ok}

import scala.beans.BeanProperty

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
          .tpl("/tpl/game/detail/index.vm")
          .process(root)


        println(g)

        ok(str).as("text/html; charset=utf-8")
      case None => notFound()
    }
  }

  def info2(id: Int) = {
    GameFullQuery().where(Filters.eq(id)).one() match {
      case Some(g) =>


//        val root = new VelocityContext()
//
//        root.put("IMG_REMOTE", Config.IMG_REMOTE)
//        root.put("GetchuGameLocal", GetchuGameLocal)
//        root.put("LOCAL", GameLocation.LOCAL)
//        root.put("DateUtil", DateUtil)
//        root.put("Strings", Strings)
//        root.put("g", g)
//
//
//        val str = VelocityTemplateConfig
//          .tpl("/tpl/game/detail/index.vm")
//          .process(root)


        println(g)

        ok(Json.toJson(g)).as("application/json; charset=utf-8")
      case None => notFound()
    }
  }

  def queryGET(request: Request) = {

    val where = request.queryString("filter").orElseThrow()

    val tpl = request.queryString("tpl").orElseThrow()

    println(where)

    val list = GameFullQuery().where(BsonDocument.parse(where)).list()

//    println(list)

    val root = new VelocityContext()

    root.put("IMG_REMOTE", Config.IMG_REMOTE)
    root.put("GetchuGameLocal", GetchuGameLocal)
    root.put("LOCAL", GameLocation.LOCAL)
    root.put("DateUtil", DateUtil)
    root.put("gamelist", list)

    val str = VelocityTemplateConfig
      .tpl(s"/tpl/game/explorer/${tpl}.html")
      .process(root)

    //    val input = """{"id" : 1111,"name":"abc"}""";

    //    val a = Json.fromJson(Json.parse(json), classOf[A])
    //
    //    println(a.id)
    //    println(a.name)


    //    request.queryString().forEach((k, v) => println(s"$k=${v.flatten.mkString(" ")}"))
    //    Filters.and(
    //      Filters.eq()
    //    )

    ok(str).as("text/html; charset=utf-8")

  }

  def queryPOST(request: Request) = {

    //    val json = request.body().asJson()
    //
    //

    val body = request.body().asText()
    println(s"Body:${body}")

    //    val json = request.queryString("json").orElseThrow()

    //    val input = """{"id" : 1111,"name":"abc"}""";

    //    val a = Json.fromJson(Json.parse(request.body().asText()), classOf[A])
    val a = Json.fromJson(Json.parse(body), classOf[A])

    println(a.id)
    println(a.name)


    //    request.queryString().forEach((k, v) => println(s"$k=${v.flatten.mkString(" ")}"))
    //    Filters.and(
    //      Filters.eq()
    //    )

    ok()

  }


}

class A {
  @BeanProperty var id: Int = _
  @BeanProperty var name: String = _
}
