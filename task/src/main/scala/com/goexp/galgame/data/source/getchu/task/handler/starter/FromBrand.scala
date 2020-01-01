package com.goexp.galgame.data.source.getchu.task.handler.starter

import com.goexp.galgame.data.source.getchu.task.handler.DownloadPage
import com.goexp.piplline.core.Starter
import com.typesafe.scalalogging.Logger

class FromBrand(val brandId: Int) extends Starter {
  private val logger = Logger(classOf[FromBrand])

  override def process() = {
    sendTo[DownloadPage]((brandId, "doujin"))
  }
}