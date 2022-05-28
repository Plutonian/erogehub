package entity.group

import scala.beans.BeanProperty

case class SampleCVItem(
                         @BeanProperty title: String,
                         @BeanProperty count: Int,
                         @BeanProperty real: Boolean
                       ) extends DataItem
