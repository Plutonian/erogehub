package com.goexp.galgame.gui.view.game.detailview.outer

import com.goexp.galgame.gui.model.Game
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML

class OutPageController extends DefaultController {
  @FXML var footerController: ControllBarController = _


  def load(game: Game) = {
    logger.info("====================================summery====================================")
    logger.info(s"Game[${game.id}] ${game.name} Date:${game.publishDate} img:${game.smallImg}  state:<${
      Option(game.state).map {
        _.get
      }.getOrElse("--")
    }>")
    logger.info(s"gameCharacter:${
      Option(game.gameCharacters).map {
        _.size()
      }.getOrElse(0)
    }")
    logger.info(s"sampleImages:${
      Option(game.gameImgs).map {
        _.size()
      }.getOrElse(0)
    }")
    logger.info("====================================summery====================================")

    loadInnerPage(game)
  }

  private def loadInnerPage(g: Game) = {
    footerController.load(g)
  }
}