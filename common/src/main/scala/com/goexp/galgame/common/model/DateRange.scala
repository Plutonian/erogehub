package com.goexp.galgame.common.model

import java.time.LocalDate
import java.util.StringJoiner
import scala.beans.BeanProperty

case class DateRange(
                      @BeanProperty start: LocalDate,
                      @BeanProperty end: LocalDate
                    ) {
  override def toString: String = new StringJoiner(", ", classOf[DateRange].getSimpleName + "[", "]").add("start=" + start).add("end=" + end).toString
}