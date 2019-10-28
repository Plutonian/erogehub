package com.goexp.galgame.gui.view.game.detailview.outer

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.game.detailview.inner.InnerPageController
import javafx.fxml.FXML

class OutPageController extends DefaultController {
  @FXML var innerPageController: InnerPageController = _
  @FXML var headerController: ControllBarController = _
  @FXML var topController: TopController = _

  override protected def initialize() = {
  }

  def load(game: Game) = {
    loadInnerPage(game)
    logger.info("Detail:{}", game)
  }

  def loadInnerPage(g: Game) = {
    topController.load(g)
    innerPageController.load(g)
    headerController.load(g)
  }
}