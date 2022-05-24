package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.task.game.search.ByTag
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{Controller, TabManager}
import com.goexp.galgame.gui.view.common.control.DataTab
import com.mongodb.client.model.Filters
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import scalafx.Includes._
import scalafx.scene.image.ImageView
import scalafx.scene.layout.BorderPane


class TagView extends BorderPane with Controller {

  val webView = new WebView()

  object Page {

    def queryByTag(tag: String) = {
      TabManager().open(tag,
        new DataTab(ExplorerData(new ByTag(tag), Filters.eq("tag", tag))) {
          text = tag
          graphic = new ImageView(LocalRes.TAG_16_PNG)
        }
      )
    }
  }


  //Init





  center = (webView)

  override def load(): Unit = {

    webView.getEngine.documentProperty().onChange((_, _, _) => {
      val win = webView.getEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
      win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
    })
    webView.getEngine.load(s"http://localhost:9000/server/tags")
  }
}
