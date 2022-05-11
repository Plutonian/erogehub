package com.goexp.galgame.gui.task.game.panel.group.node

import scala.beans.BeanProperty

case class SampleItem(
                       @BeanProperty title: String,
                       @BeanProperty count: Int
                     ) extends DataItem
