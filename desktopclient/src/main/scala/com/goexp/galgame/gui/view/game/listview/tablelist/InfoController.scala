package com.goexp.galgame.gui.view.game.listview.tablelist

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.CommonGame.Titles
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.view.common.control.StarRatingView
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Label}
import javafx.scene.layout.HBox
import javafx.scene.text.{Text, TextFlow}

class InfoController extends DefaultController {
  @FXML private var brandJumpController: JumpBrandController = _

  @FXML var txtName: Hyperlink = _
  @FXML private var txtSubName: Text = _

  @FXML private var ratingView: StarRatingView = _

  @FXML private var boxTag: HBox = _

  @FXML var textFlow: TextFlow = _

  private var targetGame: Game = _

  //  @FXML private var linkView: Hyperlink = _

  override protected def initialize(): Unit = {
    txtName.setOnAction(_ => HGameApp.loadDetail(targetGame))
  }

  def load(game: Game): Unit = {
    this.targetGame = game


    brandJumpController.load(game.brand)

    val Titles(mainTitle, subTitle) = game.getTitles
    txtName.setText(mainTitle)

    if (!Strings.isEmpty(subTitle)) {
      txtSubName.setVisible(true)
      txtSubName.setManaged(true)
      txtSubName.setText(subTitle)
    } else {
      txtSubName.setVisible(false)
      txtSubName.setManaged(false)
    }


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
