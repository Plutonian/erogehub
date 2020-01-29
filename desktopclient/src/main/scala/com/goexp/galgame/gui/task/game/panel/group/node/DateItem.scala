package com.goexp.galgame.gui.task.game.panel.group.node

import java.time.LocalDate

import com.goexp.galgame.common.model.DateRange

case class DateItem(title: String,
                    range: DateRange,
                    count: Int,
                    dateType: DateType) extends DataItem {

  def this(title: String, start: LocalDate, end: LocalDate, count: Int, dateType: DateType) {
    this(title, new DateRange(start, end), count, dateType)
  }
}