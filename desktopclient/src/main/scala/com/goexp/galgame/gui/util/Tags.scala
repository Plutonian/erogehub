package com.goexp.galgame.gui.util

import javafx.scene.Node
import javafx.scene.control.Label

import java.util
import java.util.Objects
import scala.jdk.CollectionConverters._

object Tags {
  type NodeMaker = String => Node

  implicit val maker: NodeMaker = str => {
    val tagLabel = new Label(str)
    tagLabel.getStyleClass.add("tag")
    tagLabel
  }


  def toNodes(tag: String): Node = {
    Objects.requireNonNull(tag)

    toNodes(util.List.of(tag)).get(0)

  }

  def toNodes(tag: util.List[String])(implicit mapper: NodeMaker): util.List[Node] = {
    Objects.requireNonNull(tag)
    Objects.requireNonNull(mapper)
    tag.asScala.to(LazyList)
      .filter {
        _.nonEmpty
      }
      .map(mapper)
      .asJava
  }

  def toNodes(tag: List[String])(implicit mapper: NodeMaker) = {
    Objects.requireNonNull(tag)
    Objects.requireNonNull(mapper)

    tag.to(LazyList)
      .filter {
        _.nonEmpty
      }
      .map(mapper)
  }
}