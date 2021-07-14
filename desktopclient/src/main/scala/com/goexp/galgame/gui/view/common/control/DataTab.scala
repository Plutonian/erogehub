package com.goexp.galgame.gui.view.common.control

import com.goexp.galgame.gui.util.Datas
import javafx.scene.Node
import javafx.scene.control.Tab

class DataTab(content: Node with Datas) extends Tab with Datas {

  setContent(content)

  override def load(): Unit = {

    content.load()
  }
}
