package com.goexp.galgame.gui.view.common.control

import com.goexp.galgame.gui.util.Controller
import javafx.scene.Node
import javafx.scene.control.Tab

class DataTab(content: Node with Controller) extends Tab with Controller {

  setContent(content)

  override def load(): Unit = {
    content.load()
  }

  override def dispose(): Unit = {
    content.dispose()
  }
}
