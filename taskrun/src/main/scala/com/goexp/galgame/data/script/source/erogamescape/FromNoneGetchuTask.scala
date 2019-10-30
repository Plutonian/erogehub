package com.goexp.galgame.data.script.source.erogamescape

import com.goexp.galgame.data.source.erogamescape.task.game.{GetGameBasic, Html2GameBasic, SaveGameBasic}
import com.goexp.galgame.data.source.erogamescape.task.starter.FromNoneGetchu
import com.goexp.piplline.core.Pipeline

object FromNoneGetchuTask {
  def main(args: Array[String]): Unit = {
    //    Network.initProxy()

    new Pipeline(new FromNoneGetchu)
      .regForIOType(new GetGameBasic)
      .regForCPUType(new Html2GameBasic)
      .regForIOType(new SaveGameBasic)
      .start()
  }
}