package com.goexp.galgame.gui.view.common.control

import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.ObjectProperty

import java.util
import scala.beans.BeanProperty


class TagPanel extends Control {

  @BeanProperty
  lazy val tagsProperty = new ObjectProperty[util.List[String]](this, "tags", null)

  def tags() = {
    tagsProperty.get()
  }

  def tags(tag: util.List[String]) = {
    tagsProperty.set(tag)
  }

  def this(tag: util.List[String]) = {
    this()
    tags(tag)
  }


  override def createDefaultSkin(): Skin[_] = new TagPanelSkin(this)
}


