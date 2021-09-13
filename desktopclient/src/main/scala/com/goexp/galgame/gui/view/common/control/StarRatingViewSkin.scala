package com.goexp.galgame.gui.view.common.control

import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.common.control.StarRatingViewSkin.genImages
import javafx.scene.control.SkinBase
import scalafx.scene.image.ImageView
import scalafx.Includes._
import scalafx.scene.layout.HBox

class StarRatingViewSkin(control: StarRatingView) extends SkinBase[StarRatingView](control) {
  private lazy val container = new HBox()

  init()

  private def init() = {
    control.setMouseTransparent(true)

    getChildren.setAll(container)
    reCreate()

    registerChangeListener(control.ratingProperty, {
      _ => reCreate()
    })
  }


  def reCreate() = {

    container.children = genImages(control.rating())

  }

}

object StarRatingViewSkin {
  private val _image = LocalRes.HEART_16_PNG

  private def genImages(i: Int) = {
    (0 until i).map { _ => new ImageView(_image) }
  }
}
