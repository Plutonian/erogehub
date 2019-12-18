package com.goexp.galgame.gui.view.game.detailview.inner

import com.goexp.common.util.string.Strings
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.{DefaultController, MainController}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Label, TextArea}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{FlowPane, Region}
import javafx.scene.text.Text

class HeaderPartController extends DefaultController {
  @FXML private var right: Region = _
  @FXML private var imageImg: ImageView = _
  @FXML private var flowPainter: FlowPane = _
  @FXML private var txtWriter: Label = _
  @FXML private var txtIntro: Text = _
  @FXML private var txtStory: TextArea = _
  private var targetGame: Game = _

  override protected def initialize() = {
    txtIntro.wrappingWidthProperty.bind(right.widthProperty.subtract(10))
    txtStory.prefWidthProperty.bind(right.widthProperty.subtract(10))

    flowPainter.addEventFilter(ActionEvent.ACTION, (event: ActionEvent) => {
      event.getTarget match {
        case painter: Hyperlink =>
          val str = painter.getText.replaceAll("（[^）]+）", "")
          MainController().loadPainterTab(str)
        case _ =>
      }
    })
  }

  def load(game: Game) = {
    this.targetGame = game
    loadWithoutImage(game)
    if (game.isOkImg)
      setImage(new GameImage(game).large)
    else
      setImage(null)
  }

  private def setImage(image: Image) = {
    imageImg.setImage(image)
    if (image != null) imageImg.setFitWidth(image.getWidth)
  }

  private def loadWithoutImage(game: Game) = {
    this.targetGame = game

    flowPainter.getChildren.setAll(Tags.toNodes(game.painter, (str: String) => new Hyperlink(str)))
    txtWriter.setText(String.join(",", game.writer))

    if (Strings.isNotEmpty(game.intro)) txtIntro.setText(game.intro + "\n\n")
    txtStory.setText(game.story)
  }
}