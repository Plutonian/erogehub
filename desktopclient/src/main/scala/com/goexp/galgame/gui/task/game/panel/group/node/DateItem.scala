package com.goexp.galgame.gui.task.game.panel.group.node

import java.time.LocalDate

import com.goexp.galgame.common.model.DateRange

class DateItem(private[this] val title: String,
               val range: DateRange,
               private[this] val count: Int,
               val dateType: DateType) extends DefaultItem(title, count) {

  def this(title: String, start: LocalDate, end: LocalDate, count: Int, dateType: DateType) {
    this(title, new DateRange(start, end), count, dateType)
  }
}