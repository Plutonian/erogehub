package com.goexp.galgame.data.source.getchu.task.handler.starter

import java.time.LocalDate

import com.goexp.galgame.data.source.getchu.task.handler.DownloadPage
import com.goexp.piplline.core.Starter
import com.typesafe.scalalogging.Logger

class FromDateRange(val start: LocalDate, val end: LocalDate) extends Starter {
  private val logger = Logger(classOf[FromDateRange])

  override def process() = {
    sendTo(classOf[DownloadPage], (start, end))
  }
}