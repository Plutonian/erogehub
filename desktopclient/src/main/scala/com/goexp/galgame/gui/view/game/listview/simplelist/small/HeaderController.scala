package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.common.util.string.ConsoleColors.RED
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.MainController
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController
import com.goexp.galgame.gui.view.game.part.StateChangeController
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.{Hyperlink, Label}
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.{Image, ImageView}

class HeaderController extends DefaultController {
  @FXML private var dateviewController: DateShowController = _
  @FXML private var infoController: InfoController = _

  @FXML private var changeStateController: StateChangeController = _


  @FXML private var linkView: Hyperlink = _
  @FXML private var imageImg: ImageView = _

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

    imageImg.setEffect {
      if ((game.state.get eq GameState.BLOCK) || (game.state.get eq GameState.SAME))
        new ColorAdjust(0, -1, 0, 0)
      else null
    }
  }

  def setImage(image: Image) = imageImg.setImage(image)

  private def loadWithoutImage(game: Game) = {

    this.targetGame = game

    infoController.load(game)
    changeStateController.load(game)


    dateviewController.load(game.publishDate)


    lbLoc.setText {
      if (game.location.get() != GameLocation.REMOTE)
        (game.location.get().name)
      else null
    }

  }
}