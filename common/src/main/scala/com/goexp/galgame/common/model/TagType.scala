package com.goexp.galgame.common.model

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._

case class TagType(
                    @BeanProperty name: String,
                    @BeanProperty order: Int,
                    @BeanProperty tags: List[String]
                  ) {
  def javaTag() = {
    tags.asJava
  }
}