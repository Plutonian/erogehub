package com.goexp.galgame.gui.view.common.control

import com.goexp.galgame.gui.util.res.LocalRes
import javafx.scene.control.SkinBase
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

import scala.jdk.CollectionConverters._

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

    import StarRatingViewSkin.image

    container.getChildren.setAll {
      (1 to control.rating()).map { _ => new ImageView(image) }.asJava
    }
  }

  //  override def computeMaxWidth(height: Double, topInset: Double, rightInset: Double, bottomInset: Double, leftInset: Double): Double =
  //    super.computeMaxWidth(height, topInset, rightInset, bottomInset, leftInset)
}

object StarRatingViewSkin {
  private val image = LocalRes.HEART_16_PNG
}
