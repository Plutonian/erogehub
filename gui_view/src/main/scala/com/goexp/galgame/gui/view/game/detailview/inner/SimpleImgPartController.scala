package com.goexp.galgame.gui.view.game.detailview.inner

import java.util.Objects

import com.goexp.galgame.common.model.game.GameImg
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.res.gameimg.SimpleImage
import com.goexp.galgame.gui.view.DefaultController
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{ListCell, ListView}
import javafx.scene.image.ImageView

import scala.jdk.CollectionConverters._

class SimpleImgPartController extends DefaultController {
  private var game: Game = _
  @FXML private var listSmallSimple: ListView[GameImg] = _
  @FXML private var largeSimple: ImageView = _

  override protected def initialize() = {
    listSmallSimple.setCellFactory(_ => new ListCell[GameImg]() {
      override protected def updateItem(item: GameImg, empty: Boolean) = {
        super.updateItem(item, empty)
        setGraphic(null)
        setText(null)
        if (!empty) {
          val image = new SimpleImage(game).small(item.index, item.src)
          setGraphic(new ImageView(image))
        }
      }
    })
    listSmallSimple.getSelectionModel.selectedItemProperty.addListener((_, _, simpleLargeImage) => {
      if (simpleLargeImage != null) {
        val img = new SimpleImage(game).large(simpleLargeImage.index, simpleLargeImage.src)
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