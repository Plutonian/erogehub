package com.goexp.galgame.gui.view.game.detailview.inner

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.Region

class HeaderPartController extends DefaultController {
  @FXML private var imageImg: ImageView = _
  @FXML private var txtStory: TextArea = _
  private var targetGame: Game = _

  override protected def initialize() = {
  }

  def load(game: Game) = {
    this.targetGame = game
    loadWithoutImage(game)
    if (game.isOkImg)
      setImage(GameImage(game).large())
    else
      setImage(null)
  }

  private def setImage(image: Image) = {
    imageImg.setImage(image)
    if (image != null) imageImg.setFitWidth(image.getWidth)
  }

  private def loadWithoutImage(game: Game) = {
    this.targetGame = game

    txtStory.setText(game.story)
  }
}