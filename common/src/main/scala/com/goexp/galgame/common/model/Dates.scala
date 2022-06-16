package com.goexp.galgame.common.model

import java.time.LocalDate
import scala.beans.BeanProperty

case class DateItem(
                     @BeanProperty name: String,
                     @BeanProperty index: Int,
                     @BeanProperty range: DateRange,
                   )

object Dates {

  val THIS_YEAR = LocalDate.now().getYear
  val THIS_MONTH = LocalDate.now().getMonthValue


  def monthOfThisYear() = {
    months(LocalDate.now().getYear)
  }

  def yearsIn5() = {
    val thisYear = LocalDate.now().getYear

    ((thisYear - 4) to (thisYear + 1)).reverse.map(calc)

  }

  def thisYear() = {
    val now = LocalDate.now()

    calc(now.getYear)
  }

  def thisMonth() = {
    val now = LocalDate.now()

    calc(now.getYear, now.getMonthValue)
  }

  def yearsIn10() = {
    val thisYear = LocalDate.now().getYear
    ((thisYear - 9) to (thisYear + 1)).reverse.map(calc)
  }

  def yearsIn20() = {
    val thisYear = LocalDate.now().getYear
    ((thisYear - 19) to (thisYear + 1)).reverse.map(calc)
  }

  def yearsAfter2000() = {
    val thisYear = LocalDate.now().getYear
    (2000 to (thisYear + 1)).reverse.map(calc)
  }

  def yearsAfter2010() = {
    val thisYear = LocalDate.now().getYear
    (2010 to (thisYear + 1)).reverse.map(calc)
  }

  def oldYears() = {
    val thisYear = LocalDate.now().getYear
    (2000 to 2009).reverse.map(calc)
  }

  private def months(year: Int) = {
    (1 to 12).reverse.map { m => calc(year, m) }
  }


  private def calc(year: Int) = {
    val start = LocalDate.of(year, 1, 1)
    val end = LocalDate.of(year, 12, 31)


    DateItem(
      {
        if (year == THIS_YEAR) "今年" else year.toString
      }, year,
      DateRange(start, end)
    )
  }

  private def calc(year: Int, month: Int) = {
    val start = LocalDate.of(year, month, 1)
    val end = start.plusMonths(1).minusDays(1)


    DateItem(
      {
        if (month == THIS_MONTH) "今月" else s"${month}月"
      }, month,
      DateRange(start, end)
    )
  }

}
