package com.goexp.galgame.gui.view.common.control

import javafx.beans.property.{IntegerProperty, SimpleIntegerProperty}
import javafx.scene.control.{Control, Skin}

import scala.beans.BeanProperty


class StarRatingView extends Control {

  import StarRatingView._

  @BeanProperty
  lazy val ratingProperty: IntegerProperty = new SimpleIntegerProperty(this, "rating", DEFAULT_RATING_VALUE)

  def rating() = {
    ratingProperty.get()
  }

  def rating(rate: Int) = {
    ratingProperty.set(rate)
  }

  def this(rate: Int) = {
    this()
    rating(rate)
  }


  override def createDefaultSkin(): Skin[_] = new StarRatingViewSkin(this)
}

object StarRatingView {
  private val DEFAULT_RATING_VALUE = -1
}
