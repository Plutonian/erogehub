package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.common.model.game.CommonGame.Titles
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.view.common.control.StarRatingView
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.text.Text

class InfoController extends DefaultController {
  @FXML private var brandJumpController: JumpBrandController = _

  @FXML private var txtName: Text = _
  @FXML private var txtSubName: Text = _

  @FXML private var ratingView: StarRatingView = _

  @FXML private var boxTag: HBox = _

  override protected def initialize(): Unit = {}

  def load(game: Game): Unit = {
    brandJumpController.load(game.brand)

    val Titles(mainTitle, subTitle) = game.getTitles
    txtName.setText(mainTitle)
    txtSubName.setText(subTitle)


    ratingView.ratingProperty.bind(game.star)

    if (game.tag.size > 0)
      boxTag.getChildren.setAll {
        Tags.toNodes(game.tag) { str =>
          val tagLabel = new Label(str)
          tagLabel.getStyleClass.add("tag")
          tagLabel.getStyleClass.add("tagsmall")
          tagLabel
        }
      }
    else
      boxTag.getChildren.clear()
  }
}
