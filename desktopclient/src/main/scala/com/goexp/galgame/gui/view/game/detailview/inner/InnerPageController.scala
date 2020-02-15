package com.goexp.galgame.gui.view.game.detailview.inner

import java.util.Objects

import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.gui.model.Game
import com.goexp.ui.javafx.{DefaultController, FXMLLoaderProxy}
import com.goexp.ui.javafx.control.cell.NodeListCell
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.{ListView, Tab, TabPane}
import javafx.scene.layout.Region

class InnerPageController extends DefaultController {
  @FXML var headerController: HeaderPartController = _
  @FXML var simpleImgController: SimpleImgPartController = _
  @FXML private var personListView: ListView[GameCharacter] = _
  @FXML private var contentTabPane: TabPane = _
  @FXML private var tabPerson: Tab = _
  @FXML private var tabSimple: Tab = _
  private var game: Game = _

  override protected def initialize() =

    personListView.setCellFactory(_ => {
      val loader = new FXMLLoaderProxy[Region, PersonCellController](classOf[PersonCellController].getResource("person_cell.fxml"))
      val controller = loader.controller
      controller.game = game

      NodeListCell[GameCharacter] { gameCharacter =>
        controller.gameChar = gameCharacter
        controller.init()
        loader.node
      }
    })

  def load(game: Game): Unit = {
    Objects.requireNonNull(game)

    this.game = game
    headerController.load(game)
    val personSize = Option(game.gameCharacters).map(_.size()).getOrElse(0)
    if (personSize == 0) contentTabPane.getTabs.remove(tabPerson)
    else personListView.setItems(FXCollections.observableList(game.gameCharacters))

    val imgsSize = Option(game.gameImgs).map(_.size()).getOrElse(0)

    if (imgsSize == 0)
      contentTabPane.getTabs.remove(tabSimple)
    else
      simpleImgController.load(game)
  }
}