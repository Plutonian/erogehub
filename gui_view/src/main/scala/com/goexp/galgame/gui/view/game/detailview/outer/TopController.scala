package com.goexp.galgame.gui.view.game.detailview.outer

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import com.goexp.galgame.gui.view.game.detailview.part.{DateShowController, StarChoiceBarController}
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.text.Text

class TopController extends DefaultController {
  @FXML private var dateviewController: DateShowController = _
  @FXML private var starChangeController: StarChoiceBarController = _
  @FXML private var brandJumpController: JumpBrandController = _
  @FXML private var boxStar: HBox = _
  @FXML private var boxTag: HBox = _
  @FXML private var txtName: Text = _
  @FXML private var txtSubName: Text = _
  private var targetGame: Game = _

  override protected def initialize() =
    starChangeController.onStarChangeProperty.addListener((_, _, changed) => {
      if (changed) loadStar(targetGame)
    })

  private def loadStar(game: Game) = {
    val image = LocalRes.HEART_16_PNG

    boxStar.getChildren.clear()
    val stars = Range(0, game.star).to(LazyList).map { _ => new ImageView(image) }.toArray
    boxStar.getChildren.addAll(stars: _*)


  }

  def load(game: Game) = loadWithoutImage(game)

  private def loadWithoutImage(game: Game) = {
    this.targetGame = game
    starChangeController.load(game)
    val titles = game.getTitles
    txtName.setText(titles.mainTitle)
    txtSubName.setText(titles.subTitle)
    dateviewController.load(game.publishDate)
    brandJumpController.load(game.brand)
    if (game.tag.size > 0) {
      val nodes = Tags.toNodes(game.tag) { str =>
        val tagLabel = new Label(str)
        tagLabel.getStyleClass.add("tag")
        tagLabel.getStyleClass.add("tagsmall")
        tagLabel
      }

      boxTag.getChildren.setAll(nodes)
    }
    else boxTag.getChildren.clear()


    val image = LocalRes.HEART_16_PNG

    boxStar.getChildren.clear()
    val stars = Range(0, game.star).to(LazyList).map { _ => new ImageView(image) }.toArray
    boxStar.getChildren.addAll(stars: _*)


  }
}