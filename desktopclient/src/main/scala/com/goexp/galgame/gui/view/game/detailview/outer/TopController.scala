package com.goexp.galgame.gui.view.game.detailview.outer

import com.goexp.galgame.common.model.game.CommonGame.Titles
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.view.common.control.FormattedDate
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import com.goexp.galgame.gui.view.game.detailview.part.StarRatingController
import com.goexp.ui.javafx.DefaultController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Label}
import javafx.scene.layout.{FlowPane, HBox}
import javafx.scene.text.Text

class TopController extends DefaultController {
  @FXML private var starRatingController: StarRatingController = _
  @FXML private var brandJumpController: JumpBrandController = _
  @FXML private var boxTag: HBox = _
  @FXML private var txtName: Text = _
  @FXML private var txtSubName: Text = _
  @FXML private var dateview: FormattedDate = _

  @FXML private var flowPainter: FlowPane = _

  private var targetGame: Game = _

  override protected def initialize() = {
    //    flowPainter.prefWrapLengthProperty().bind(right.widthProperty.subtract(10))
    flowPainter.addEventFilter(ActionEvent.ACTION, (event: ActionEvent) => {
      event.getTarget match {
        case painter: Hyperlink =>
          val str = painter.getText.replaceAll("（[^）]+）", "")
          HGameApp.loadPainterTab(str)
        case _ =>
      }
    })
  }

  def reset() = {
    txtName.setText(null)
    txtSubName.setText(null)

    boxTag.getChildren.clear()
  }

  def load(game: Game) = {
    reset()
    loadWithoutImage(game)
  }

  private def loadWithoutImage(game: Game) = {
    this.targetGame = game

    starRatingController.load(game)

    dateview.date(game.publishDate)
    brandJumpController.load(game.brand)

    val Titles(mainTitle, subTitle) = game.getTitles
    txtName.setText(mainTitle)
    txtSubName.setText(subTitle)


    if (game.tag.size > 0) {
      val nodes = Tags.toNodes(game.tag) { str =>
        val tagLabel = new Label(str)
        tagLabel.getStyleClass.add("tag")
        tagLabel.getStyleClass.add("tagsmall")
        tagLabel
      }

      boxTag.getChildren.setAll(nodes)
    }

    flowPainter.getChildren.setAll(Tags.toNodes(game.painter) {
      new Hyperlink(_)
    })
  }
}