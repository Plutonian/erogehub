package com.goexp.galgame.data.source.getchu.task.handler.starter

import com.goexp.galgame.data.source.getchu.task.handler.DownloadPage
import com.goexp.piplline.core.Starter
import org.slf4j.LoggerFactory

class FromBrand(val brandId: Int) extends Starter {
  private val logger = LoggerFactory.getLogger(classOf[FromBrand])

  override def process() = {
    sendTo(classOf[DownloadPage], (brandId, "BrandList"))
  }
}