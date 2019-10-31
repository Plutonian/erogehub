package com.goexp.galgame.data.source.getchu.task.handler.starter

import java.time.LocalDate

import com.goexp.galgame.data.source.getchu.task.handler.DownloadPage
import com.goexp.piplline.core.Starter
import org.slf4j.LoggerFactory

class FromDateRange(val start: LocalDate, val end: LocalDate) extends Starter {
  private val logger = LoggerFactory.getLogger(classOf[FromDateRange])

  override def process() = {
    sendTo(classOf[DownloadPage], (start, end))
  }
}