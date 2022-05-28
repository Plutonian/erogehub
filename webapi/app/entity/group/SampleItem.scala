package entity.group

import scala.beans.BeanProperty

case class SampleItem(
                       @BeanProperty title: String,
                       @BeanProperty count: Int
                     ) extends DataItem
