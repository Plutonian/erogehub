package com.goexp.galgame.data.util

import java.time.LocalDate

import com.goexp.galgame.common.model.DateRange

object DateRangeUtil {
  def yearToNow = new DateRange(LocalDate.now.withDayOfYear(1), LocalDate.now)

  def monthToNow = new DateRange(LocalDate.now.withDayOfMonth(1), LocalDate.now)
}