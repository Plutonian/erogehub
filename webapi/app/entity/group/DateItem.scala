package entity.group

import com.goexp.galgame.common.model.DateRange

import java.time.LocalDate
import scala.beans.BeanProperty

case class DateItem(
                     @BeanProperty title: String,
                     @BeanProperty range: DateRange,
                     @BeanProperty index: Int,
                     @BeanProperty count: Int,
                     @BeanProperty dateType: DateType,
                     @BeanProperty children: Array[DateItem]
                   ) extends DataItem {

  def this(title: String, start: LocalDate, end: LocalDate, index: Int, count: Int, dateType: DateType, children: Array[DateItem]) = {
    this(title, DateRange(start, end), index, count, dateType, children)
  }
}
