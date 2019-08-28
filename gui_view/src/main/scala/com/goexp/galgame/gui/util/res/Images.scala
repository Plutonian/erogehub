package com.goexp.galgame.gui.util.res

import java.io.{ByteArrayOutputStream, IOException}
import java.util.Objects

import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import javax.imageio.ImageIO

object Images {
  def getImageBytes(image: Image): Array[Byte] = {
    Objects.requireNonNull(image)
    val bufferImage = SwingFXUtils.fromFXImage(image, null)
    if (bufferImage == null) return null

    val stream = new ByteArrayOutputStream
    try {
      ImageIO.write(bufferImage, "jpg", stream)
      return stream.toByteArray
    } catch {
      case e: IOException =>
        e.printStackTrace()
    } finally {
      stream.close()
    }
    null
  }
}
