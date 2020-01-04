package com.goexp.galgame.gui.util.res

import javafx.scene.image.Image

object LocalRes {
  val BRAND_16_PNG = LocalRes("/ico/16/brand_16.png")
  val SEARCH_16_PNG = LocalRes("/ico/16/search_16.png")
  val DATE_16_PNG = LocalRes("/ico/16/date_16.png")
  val CV_16_PNG = LocalRes("/ico/16/cv_16.png")
  val GAME_16_PNG = LocalRes("/ico/16/game_16.png")
  val TAG_16_PNG = LocalRes("/ico/16/tag_16.png")
  val HEART_16_PNG = LocalRes("/ico/16/heart_16.png")
  val HEART_32_PNG = LocalRes("/ico/32/heart_32.png")

  val IMG_DATE_PNG = LocalRes("/img/date.png")
  val IMG_CV_PNG = LocalRes("/img/cv.png")
  val IMG_search_PNG = LocalRes("/img/search.png")
  val IMG_TAG_PNG = LocalRes("/img/tag.png")

  def apply(path: String): Image = new Image(this.getClass.getResource(path).toExternalForm)

}

//final class LocalRes private(val path: String) {
//
//  def get: Image =new Image(this.getClass.getResource(path).toExternalForm)
//
//}