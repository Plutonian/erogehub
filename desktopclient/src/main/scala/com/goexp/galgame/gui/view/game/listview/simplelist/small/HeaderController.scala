package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.galgame.common.model.game.CommonGame.Titles
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.MainController
import com.goexp.galgame.gui.view.common.control.StarRatingView
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController
import com.goexp.galgame.gui.view.game.part.StateChangeController
import com.goexp.ui.javafx.DefaultController
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
  @FXML private var ratingView: StarRatingView = _
  @FXML private var txtName: Text = _
  @FXML private var txtSubName: Text = _
  @FXML private var boxTag: HBox = _
  @FXML private var lbLoc: Label = _

  private var targetGame: Game = _

  override protected def initialize() = {
    linkView.setOnAction(_ => MainController().loadDetail(targetGame))

  }

  def load(game: Game) = {
    logger.debug(s"Game[${RED.s(game.id.toString)}] ${RED.s(game.name)} Date:${game.publishDate} img:${game.smallImg}  state:<${
      Option(game.state).map {
        _.get
      }.getOrElse("--")
    }>")


    loadWithoutImage(game)


    imageImg.setImage {
      if (game.isOkImg) GameImage(game).tiny() else null
    }
  }

  def setImage(image: Image) = imageImg.setImage(image)

  private def loadWithoutImage(game: Game) = {

    this.targetGame = game
    changeStateController.load(game)
    brandJumpController.load(game.brand)

    val Titles(mainTitle, subTitle) = game.getTitles
    txtName.setText(mainTitle)
    txtSubName.setText(subTitle)

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

    dateviewController.load(game.publishDate)

    ratingView.ratingProperty.bind(game.star)


    lbLoc.setText {
      if (game.location.get() != GameLocation.REMOTE)
        (game.location.get().name)
      else null
    }
    imageImg.setEffect {
      if ((game.state.get eq GameState.BLOCK) || (game.state.get eq GameState.SAME))
        new ColorAdjust(0, -1, 0, 0)
      else null
    }

  }
}