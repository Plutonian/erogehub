package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import com.goexp.galgame.gui.view.game.HomeController
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController
import com.goexp.galgame.gui.view.game.part.StateChangeController
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Label}
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.HBox
import javafx.scene.text.Text

class HeaderController extends DefaultController {
  @FXML private var dateviewController: DateShowController = _
  @FXML private var brandJumpController: JumpBrandController = _
  @FXML private var changeStateController: StateChangeController = _
  @FXML private var linkView: Hyperlink = _
  @FXML private var imageImg: ImageView = _
  @FXML private var boxStar: HBox = _
  @FXML private var txtName: Text = _
  @FXML private var txtSubName: Text = _
  @FXML private var boxTag: HBox = _
  private var targetGame: Game = _

  override protected def initialize() =
    linkView.setOnAction(_ => HomeController.$this.loadDetail(targetGame))

  def load(game: Game) = {
    loadWithoutImage(game)
    if (game.isOkImg)
      imageImg.setImage(new GameImage(game).tiny)
    else
      imageImg.setImage(null)
  }

  def setImage(image: Image) = imageImg.setImage(image)

  private def loadWithoutImage(game: Game) = {

    this.targetGame = game
    changeStateController.load(game)
    brandJumpController.load(game.brand)
    val titles = game.getTitles
    txtName.setText(titles.mainTitle)
    txtSubName.setText(titles.subTitle)

    if (game.tag.size > 0)
      boxTag.getChildren.setAll(
        Tags.toNodes(game.tag, (str: String) => {
          val tagLabel = new Label(str)
          tagLabel.getStyleClass.add("tag")
          tagLabel.getStyleClass.add("tagbig")
          tagLabel
        }))
    else
      boxTag.getChildren.clear()

    dateviewController.load(game.publishDate)

    val image = LocalRes.HEART_16_PNG

    boxStar.getChildren.clear()
    val stars = Range(0, game.star).to(LazyList).map { _ => new ImageView(image) }.toArray
    boxStar.getChildren.addAll(stars: _*)


    if ((game.state.get eq GameState.BLOCK) || (game.state.get eq GameState.SAME))
      imageImg.setEffect(new ColorAdjust(0, -1, 0, 0))
    else
      imageImg.setEffect(null)
  }
}