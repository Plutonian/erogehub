package com.goexp.galgame.gui.view.common.control

import com.goexp.galgame.gui.util.Controller
import scalafx.scene.Node
import scalafx.scene.control.Tab
//import javafx.scene.Node
//import javafx.scene.control.Tab

//class DataTab(content: javafx.scene.Node with Controller) extends javafx.scene.control.Tab with Controller {
//
//  setContent(content)
//
//  override def load(): Unit = {
//    content.load()
//  }
//
//  override def dispose(): Unit = {
//    content.dispose()
//  }
//}

class DataPage(c: Node with Controller) extends Tab with Controller {

  content = c

  override def load(): Unit = {
    c.load()
  }

  override def dispose(): Unit = {
    c.dispose()
  }
}
