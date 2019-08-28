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

  def apply(path: String): LocalRes = new LocalRes(path)

}

final class LocalRes private(val path: String) {
  private[this] var cacheImage: Image = _

  def get: Image =
    if (cacheImage == null) {
      cacheImage = new Image(this.getClass.getResource(path).toExternalForm)

      cacheImage
    }
    else
      cacheImage
}