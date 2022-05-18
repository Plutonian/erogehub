package com.goexp.galgame.common.model

import java.time.LocalDate
import java.util.StringJoiner

case class DateRange(start: LocalDate, end: LocalDate) {
  override def toString: String = new StringJoiner(", ", classOf[DateRange].getSimpleName + "[", "]").add("start=" + start).add("end=" + end).toString
}