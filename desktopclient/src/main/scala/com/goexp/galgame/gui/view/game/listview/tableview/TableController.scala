package com.goexp.galgame.gui.view.game.listview.tableview

import java.time.LocalDate
import java.util

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.change.MultiLike
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.{DefaultController, MainController}
import com.goexp.javafx.cell.{NodeTableCell, TextTableCell}
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox

import scala.jdk.CollectionConverters._

class TableController extends DefaultController {
  @FXML var table: TableView[Game] = _

  @FXML var tableColDate: TableColumn[Game, LocalDate] = _
  @FXML var tableColStar: TableColumn[Game, Int] = _
  @FXML var tableColBrand: TableColumn[Game, Brand] = _
  @FXML var tableColTitle: TableColumn[Game, String] = _
  @FXML var tableColType: TableColumn[Game, String] = _
  @FXML var tableColPainter: TableColumn[Game, String] = _
  @FXML var tableColWriter: TableColumn[Game, String] = _
  @FXML var tableColState: TableColumn[Game, GameState] = _
  @FXML var tableColLocation: TableColumn[Game, GameLocation] = _

  @FXML private var menuPopup: ContextMenu = _
  private var selectedGames: util.List[Game] = _
  final private val changeGameService = TaskService(new MultiLike(selectedGames))

  override protected def initialize() = {

    val items =
      GameState.values.to(LazyList).reverse
        .map(state => {
          val menuItem = new MenuItem
          menuItem.setUserData(state)
          menuItem.setText(state.name)
          menuItem.setOnAction(e => {
            val gameState = ((e.getSource).asInstanceOf[MenuItem]).getUserData.asInstanceOf[GameState]

            logger.debug(s"<MenuItem>:${gameState.name}")

            selectedGames = table.getSelectionModel.getSelectedItems
            selectedGames.forEach(_.state.set(gameState))
            changeGameService.restart()
          })
          menuItem
        }).asJava

    menuPopup.getItems.setAll(items)
    table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)
    table.setRowFactory(_ => {
      val row = new TableRow[Game]
      row.setOnMouseClicked(null)
      row.setOnMouseClicked(event => {
        if ((event.getButton eq MouseButton.PRIMARY) && event.getClickCount == 2)
          if (!row.isEmpty) {
            val g = row.getItem
            MainController().loadDetail(g)
          }
      })
      row
    })
    tableColLocation.setCellValueFactory(param => param.getValue.location)
    tableColState.setCellValueFactory(param => param.getValue.state)
    tableColBrand.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.brand))
    tableColStar.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.star))
    tableColPainter.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.getPainter))
    tableColWriter.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.getWriter))
    tableColDate.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.publishDate))
    tableColTitle.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.name))
    tableColType.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.getType))

    tableColDate.setCellFactory { _ =>
      TextTableCell { publishDate =>
        DateUtil.formatDate(publishDate)
      }
    }

    tableColBrand.setCellFactory(_ =>
      TextTableCell { brand =>
        brand.name
      }
    )

    tableColTitle.setCellFactory(_ =>
      TextTableCell { name =>
        name.replaceAll("＜[^＞]*＞", "")
      }
    )

    tableColLocation.setCellFactory(_ =>
      TextTableCell { location =>
        location.name
      })

    tableColState.setCellFactory(_ => new com.goexp.javafx.cell.TableCell[Game, GameState]() {

      override protected def notEmpty(gameState: GameState): Unit = {
        if (gameState eq GameState.BLOCK)
          this.getTableRow.getStyleClass.add("gray")

        this.setText(gameState.name)
      }


      override protected def updateItem(item: GameState, empty: Boolean) = {

        this.getTableRow.getStyleClass.remove("gray")
        super.updateItem(item, empty)

      }
    })


    tableColStar.setCellFactory(_ =>
      NodeTableCell { star =>
        val image = LocalRes.HEART_16_PNG

        val stars = Range(0, star).to(LazyList).map { _ => new ImageView(image) }.toArray
        new HBox(stars: _*)

      }
    )
  }
}