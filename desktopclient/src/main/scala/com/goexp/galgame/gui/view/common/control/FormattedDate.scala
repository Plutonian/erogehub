package com.goexp.galgame.gui.view.common.control

import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.ObjectProperty

import java.time.LocalDate
import scala.beans.BeanProperty

class FormattedDate extends Control {

  @BeanProperty
  lazy val dateProperty = new ObjectProperty[LocalDate](this, "date", null)

  def date() = {
    dateProperty.get()
  }

  def date(tag: LocalDate) = {
    dateProperty.set(tag)
  }

  def this(tag: LocalDate) = {
    this()
    date(tag)
  }


  override def createDefaultSkin(): Skin[_] = new FormattedDateSkin(this)

}