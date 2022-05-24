package controllers

import com.goexp.common.util.date.DateUtil
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.data.source.getchu.query.GameFullQuery
import com.goexp.galgame.gui.Config
import com.goexp.galgame.gui.task.game.panel.group.node.{DataItem, SampleItem}
import com.mongodb.client.model.Filters
import org.apache.velocity.VelocityContext
import org.bson.BsonDocument
import play.mvc.Controller
import play.mvc.Http.Request
import play.mvc.Results.{notFound, ok}

import scala.jdk.CollectionConverters._

class GameController extends Controller {


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


  def queryGET(request: Request) = {

    val where = request.queryString("filter").orElseThrow()

    val tpl = request.queryString("tpl").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where)).list()

    val root = new VelocityContext()

    root.put("IMG_REMOTE", Config.IMG_REMOTE)
    root.put("GetchuGameLocal", GetchuGameLocal)
    root.put("LOCAL", GameLocation.LOCAL)
    root.put("DateUtil", DateUtil)
    root.put("gamelist", list)

    val str = VelocityTemplateConfig
      .tpl(s"/tpl/game/explorer/${tpl}.vm")
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

  def sideCV(request: Request)={
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where)).scalaList()

    val cvlist = list.to(LazyList)
      .filter(g => Option(g.gameCharacters).map(_.size()).getOrElse(0) > 0)
      .flatMap { g =>
        g.gameCharacters.asScala.to(LazyList)
          .map { p => p.getShowCV() }
          .filter {
            _.isDefined
          }
          .map { c => c.get }
        //          .filter(t => Strings.isNotEmpty(t))
      }

      .groupBy(s => s).to(LazyList)
      .sortBy { case (_, v) => v.size }.reverse
      //        .take(20)
      .map { case (key, value) =>
        SampleItem(key, value.size).asInstanceOf[DataItem]

      }.asJava





    val root = new VelocityContext()

    root.put("IMG_REMOTE", Config.IMG_REMOTE)
    root.put("GetchuGameLocal", GetchuGameLocal)
    root.put("LOCAL", GameLocation.LOCAL)
    root.put("DateUtil", DateUtil)
    root.put("cvlist", cvlist)

    val str = VelocityTemplateConfig
      .tpl(s"/tpl/game/explorer/sidebar/cvlist.vm")
      .process(root)

    ok(str).as("text/html; charset=utf-8")
  }

  def sideTag(request: Request)={
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where)).scalaList()

    val taglist = list.to(LazyList)
      .filter(g => g.tag.size > 0)
      .flatMap(g => g.tag.asScala.to(LazyList).filter(t => Strings.isNotEmpty(t)))

      .groupBy(s => s).to(LazyList)
      .sortBy { case (_, v) => v.size }.reverse
      //        .take(20)
      .map { case (key, value) =>
        SampleItem(key, value.size).asInstanceOf[DataItem]
      }.asJava





    val root = new VelocityContext()

    root.put("IMG_REMOTE", Config.IMG_REMOTE)
    root.put("GetchuGameLocal", GetchuGameLocal)
    root.put("LOCAL", GameLocation.LOCAL)
    root.put("DateUtil", DateUtil)
    root.put("taglist", taglist)

    val str = VelocityTemplateConfig
      .tpl(s"/tpl/game/explorer/sidebar/taglist.vm")
      .process(root)

    ok(str).as("text/html; charset=utf-8")
  }

}

