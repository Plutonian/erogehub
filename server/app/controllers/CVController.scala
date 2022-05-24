package controllers

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.game.GameLocation
import com.goexp.galgame.common.website.getchu.GetchuGameLocal
import com.goexp.galgame.data.source.getchu.query.CVQuery
import com.goexp.galgame.gui.Config
import org.apache.velocity.VelocityContext
import play.mvc.Controller
import play.mvc.Results.ok

import scala.jdk.CollectionConverters._


class CVController extends Controller {

  def list() = {

    val list = CVQuery().scalaList()
      .groupBy(_.star).to(LazyList)
      .sortBy { case (k, _) => k }.reverse
      .map { case (k, v) => (k, v.asJava) }
      .asJava



    val root = new VelocityContext()

    root.put("IMG_REMOTE", Config.IMG_REMOTE)
    root.put("GetchuGameLocal", GetchuGameLocal)
    root.put("LOCAL", GameLocation.LOCAL)
    root.put("DateUtil", DateUtil)
    root.put("cvlist", list)

    val str = VelocityTemplateConfig
      .tpl(s"/tpl/game/cvinfo.vm")
      .process(root)

    ok(str).as("text/html; charset=utf-8")
  }

}
