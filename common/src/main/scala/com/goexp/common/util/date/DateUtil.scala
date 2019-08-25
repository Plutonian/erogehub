package com.goexp.common.util.date

import java.text.{ParseException, SimpleDateFormat}
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import java.util.Date

object DateUtil {

  def formatDate(date: LocalDate): String = {
    if (date == null) return ""
    val now = LocalDate.now
    val days = date.toEpochDay - now.toEpochDay
    if (days == 0) return "今日"
    if (days == -1) return "昨日"
    if (days == 1) return "明日"
    if (days > 0) return s"あと${days}日"
    if (days > -8) return s"${-days}日前"

    date.format(DateTimeFormatter.ofPattern("yy-MM-dd"))
  }

  def needFormat(date: LocalDate): Boolean = {
    if (date == null) return false
    val now = LocalDate.now
    val days = date.toEpochDay - now.toEpochDay
    days > -8
  }

  def toDate(dateToConvert: String): Date = {
    val format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    try return format.parse(dateToConvert)
    catch {
      case e: ParseException =>
        e.printStackTrace()
    }
    null
  }

  def toLocalDate(dateToConvert: Date): LocalDate = LocalDate.ofInstant(dateToConvert.toInstant, ZoneId.systemDefault)
}