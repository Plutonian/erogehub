package com.goexp.galgame.gui.view.game.detailview.inner

import java.util.Objects

import com.goexp.galgame.common.model.game.GameImg
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.res.gameimg.SimpleImage
import com.goexp.ui.javafx.DefaultController
import com.goexp.ui.javafx.control.cell.NodeListCell
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.image.ImageView

import scala.jdk.CollectionConverters._

class SimpleImgPartController extends DefaultController {
  private var game: Game = _
  @FXML private var listSmallSimple: ListView[GameImg] = _
  @FXML private var largeSimple: ImageView = _

  override protected def initialize() = {

    listSmallSimple.setCellFactory(_ =>
      NodeListCell[GameImg] { case GameImg(_, index) =>
        val image = new SimpleImage(game).small(index)
        new ImageView(image)
      }
    )

    listSmallSimple.getSelectionModel.selectedItemProperty.addListener((_, _, simpleLargeImage) => {
      Option(simpleLargeImage).foreach { case GameImg(_, index) =>
        val img = new SimpleImage(game).large(index)
        largeSimple.setImage(img)
      }
    })
  }

  def load(game: Game) = {
    Objects.requireNonNull(game)

    this.game = game
    listSmallSimple.getSelectionModel.clearSelection()
    largeSimple.setImage(null)
    listSmallSimple.setItems(FXCollections.observableArrayList(game.gameImgs))
    listSmallSimple.getSelectionModel.select(0)

    logger.whenDebugEnabled {
      val str = Option(game.gameImgs)
        .map { list =>
          list.asScala.to(LazyList)
            .map { img => s"$img" }
            .mkString("\n")
        }
        .map { str => s"====================================SampleImage====================================\n$str" }
        .getOrElse("No sample image")

      logger.debug(str)
    }

  }

  @FXML private def btnPrev_OnAction(event: ActionEvent) = {
    var index = listSmallSimple.getSelectionModel.getSelectedIndex
    index -= 1
    if (index >= 0) {
      listSmallSimple.getSelectionModel.select(index)
      listSmallSimple.scrollTo(index)
    }
  }

  @FXML private def btnNext_OnAction(event: ActionEvent) = {
    var index = listSmallSimple.getSelectionModel.getSelectedIndex
    index += 1
    if (index < listSmallSimple.getItems.size) {
      listSmallSimple.getSelectionModel.select(index)
      listSmallSimple.scrollTo(index)
    }
  }
}