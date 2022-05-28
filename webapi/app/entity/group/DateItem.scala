package entity.group

import com.goexp.galgame.common.model.DateRange

import java.time.LocalDate
import scala.beans.BeanProperty

case class DateItem(
                     @BeanProperty title: String,
                     range: DateRange,
                     @BeanProperty count: Int,
                     @BeanProperty dateType: DateType,
                     @BeanProperty children: Array[DateItem]
                   ) extends DataItem {

  def this(title: String, start: LocalDate, end: LocalDate, count: Int, dateType: DateType, children: Array[DateItem]) = {
    this(title, DateRange(start, end), count, dateType, children)
  }
}
