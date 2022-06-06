package com.goexp.galgame.common.model

import java.time.LocalDate
import scala.beans.BeanProperty

case class DateItem(
                     @BeanProperty name: String,
                     @BeanProperty range: DateRange,
                   )

object Dates {

  val THIS_YEAR = LocalDate.now().getYear
  val THIS_MONTH = LocalDate.now().getMonthValue

  def months(year: Int) = {
    (1 to 12).reverse.map { m => calc(year, m) }
  }

  def monthOfThisYear() = {
    months(LocalDate.now().getYear)
  }

  def yearsIn5() = {
    val thisYear = LocalDate.now().getYear

    ((thisYear - 4) to (thisYear + 1)).reverse.map(calc)

  }

  def calc(year: Int) = {
    val start = LocalDate.of(year, 1, 1)
    val end = LocalDate.of(year, 12, 31)


    DateItem(
      {
        if (year == THIS_YEAR) "今年" else year.toString
      },
      DateRange(start, end)
    )
  }

  def calc(year: Int, month: Int) = {
    val start = LocalDate.of(year, month, 1)
    val end = start.plusMonths(1).minusDays(1)


    DateItem(
      {
        if (month == THIS_MONTH) "今月" else month.toString
      },
      DateRange(start, end)
    )
  }

}
