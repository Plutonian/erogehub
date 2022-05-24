package controllers

import com.goexp.galgame.gui.db.mongo.query.TagQuery
import com.mongodb.client.model.Sorts.ascending
import org.apache.velocity.VelocityContext
import play.mvc.Controller
import play.mvc.Results.ok

class TagController extends Controller {

  def list() = {

    val list = TagQuery().sort(ascending("order")).list()

    val root = new VelocityContext()

    root.put("tagGroup", list)

    println(list)

    val str = VelocityTemplateConfig
      .tpl(s"/tpl/game/tag.vm")
      .process(root)

    ok(str).as("text/html; charset=utf-8")
  }

}
