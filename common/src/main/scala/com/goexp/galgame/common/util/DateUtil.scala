package com.goexp.galgame.common.util

import java.text.{ParseException, SimpleDateFormat}
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import java.util.Date

object DateUtil {

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