package com.goexp.galgame.data.source.getchu.task.handler.starter

import java.time.LocalDate

import com.goexp.galgame.data.source.getchu.task.handler.DownloadPage
import com.goexp.piplline.core.Starter
import com.typesafe.scalalogging.Logger

class FromDateRange(val start: LocalDate, val end: LocalDate) extends Starter {

  override def process() = {
    sendTo[DownloadPage]((start, end))
  }
}