package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.CommonGame.Titles
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.common.control.StarRatingView
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController
import com.goexp.galgame.gui.view.game.part.StateChangeController
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Label}
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{FlowPane, Pane}
import javafx.scene.text.{Text, TextFlow}

class HeaderController extends DefaultController {
  @FXML private var dateviewController: DateShowController = _
  @FXML private var dateview: Pane = _

  @FXML private var changeStateController: StateChangeController = _


  @FXML private var imageImg: ImageView = _

  @FXML private var lbLoc: Label = _

  @FXML private var brandJumpController: JumpBrandController = _

  @FXML var txtName: Hyperlink = _
  @FXML private var txtSubName: Text = _

  @FXML private var ratingView: StarRatingView = _

  @FXML private var boxTag: FlowPane = _

  @FXML var textFlow: TextFlow = _

  private var targetGame: Game = _


  override protected def initialize() = {

    txtName.setOnAction(_ => HGameApp.loadDetail(targetGame))

    textFlow.prefWidthProperty().bind(imageImg.fitWidthProperty().subtract(dateview.widthProperty()))
    boxTag.prefWidthProperty().bind(imageImg.fitWidthProperty())

  }

  def load(game: Game) = {


    logger.debug(s"Game[${RED.s(game.id.toString)}] ${RED.s(game.name)} Date:${game.publishDate} img:${game.smallImg}  state:<${
      Option(game.state).map {
        _.get
      }.getOrElse("--")
    }>")


    loadWithoutImage(game)

    if (game.isOkImg) {
      val image = GameImage(game).normal()
      txtName.setPrefWidth(image.getWidth - dateview.getWidth)
      imageImg.setImage(image)
    } else {
      imageImg.setImage(null)

    }


    imageImg.setEffect {

      if (GameState.ignoreState().contains(game.state.get))
        new ColorAdjust(0, -1, 0, 0)
      else null
    }
  }

  def setImage(image: Image) = imageImg.setImage(image)

  private def loadWithoutImage(game: Game) = {

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


    changeStateController.load(game)


    dateviewController.load(game.publishDate)


    lbLoc.setText {
      if (game.location.get() != GameLocation.REMOTE)
        (game.location.get().name)
      else null
    }

  }
}