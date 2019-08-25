package com.goexp.common.util

import java.time.format.DateTimeFormatter

package object date {
  implicit def formatDate(datetimeformat: String) = DateTimeFormatter.ofPattern(datetimeformat)
}
