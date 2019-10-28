package com.goexp.galgame.gui.util

import java.util
import java.util.Objects

import javafx.scene.Node
import javafx.scene.control.Label

import scala.jdk.CollectionConverters._

object Tags {
  def toNodes(tag: String): Node = {
    Objects.requireNonNull(tag)
    toNodes(util.List.of(tag), (str: String) => {
      val tagLabel = new Label(str)
      tagLabel.getStyleClass.add("tag")
      tagLabel
    }).get(0)
  }

  def toNodes(tag: util.List[String]): util.List[Node] = {
    Objects.requireNonNull(tag)
    toNodes(tag, (str: String) => {
      val tagLabel = new Label(str)
      tagLabel.getStyleClass.add("tag")
      tagLabel
    })
  }

  def toNodes(tag: util.List[String], mapper: String => Node): util.List[Node] = {
    Objects.requireNonNull(tag)
    Objects.requireNonNull(mapper)
    tag.asScala.to(LazyList)
      .filter((str: String) => !str.isEmpty)
      .map(mapper)
      .asJava
  }
}