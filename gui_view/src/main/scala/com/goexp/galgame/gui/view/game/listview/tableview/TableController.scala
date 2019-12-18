package com.goexp.galgame.gui.view.game.listview.tableview

import java.time.LocalDate
import java.util

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.change.MultiLike
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.view.{DefaultController, MainController}
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox

import scala.jdk.CollectionConverters._

class TableController extends DefaultController {
  @FXML var table: TableView[Game] = _
  @FXML var tableColType: TableColumn[Game, String] = _
  @FXML var tableColBrand: TableColumn[Game, Brand] = _
  @FXML var tableColPainter: TableColumn[Game, String] = _
  @FXML var tableColWriter: TableColumn[Game, String] = _
  @FXML var tableColState: TableColumn[Game, GameState] = _
  @FXML var tableColStar: TableColumn[Game, Int] = _
  @FXML var tableColDate: TableColumn[Game, LocalDate] = _
  @FXML var tableColTitle: TableColumn[Game, String] = _
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
    tableColState.setCellValueFactory(new PropertyValueFactory[Game, GameState]("state"))
    tableColBrand.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.brand))
    tableColStar.setCellValueFactory(new PropertyValueFactory[Game, Int]("star"))
    tableColPainter.setCellValueFactory(new PropertyValueFactory[Game, String]("painter"))
    tableColWriter.setCellValueFactory(new PropertyValueFactory[Game, String]("writer"))
    tableColDate.setCellValueFactory(new PropertyValueFactory[Game, LocalDate]("publishDate"))
    tableColTitle.setCellValueFactory(new PropertyValueFactory[Game, String]("name"))
    tableColType.setCellValueFactory(new PropertyValueFactory[Game, String]("type"))

    tableColDate.setCellFactory((_: TableColumn[Game, LocalDate]) => new TableCell[Game, LocalDate]() {
      override protected def updateItem(item: LocalDate, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setGraphic(null)
        this.setText(null)
        if (item != null && !empty)
          this.setText(DateUtil.formatDate(item))
      }
    })

    tableColBrand.setCellFactory((_: TableColumn[Game, Brand]) => new TableCell[Game, Brand]() {
      override protected def updateItem(brand: Brand, empty: Boolean) = {
        super.updateItem(brand, empty)
        this.setGraphic(null)
        this.setText(null)

        if (brand != null && !empty)
          this.setText(brand.name)
      }
    })

    tableColTitle.setCellFactory((_: TableColumn[Game, String]) => new TableCell[Game, String]() {
      override protected def updateItem(item: String, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setGraphic(null)
        this.setText(null)

        if (item != null && !empty) {
          val game = this.getTableRow.getItem
          if (game != null) {
            val title = game.name
            val titleLabel = title.replaceAll("＜[^＞]*＞", "")

            if (game.tag.size > 0) {
              val hbox = new HBox
              hbox.setSpacing(5)
              hbox.getChildren.setAll(Tags.toNodes(game.tag))
              this.setGraphic(new HBox(new Label(title), hbox))
            }
            else
              this.setText(titleLabel)
          }
        }
      }
    })
    tableColState.setCellFactory((_: TableColumn[Game, GameState]) => new TableCell[Game, GameState]() {
      override protected def updateItem(item: GameState, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setGraphic(null)
        this.setText(null)
        this.getTableRow.getStyleClass.remove("gray")
        if (item != null && !empty) {
          if (item eq GameState.BLOCK)
            this.getTableRow.getStyleClass.add("gray")

          this.setText(item.name)
        }
      }
    })
    tableColStar.setCellFactory((_: TableColumn[Game, Int]) => new TableCell[Game, Int]() {
      override protected def updateItem(item: Int, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setGraphic(null)
        this.setText(null)
        if (!empty) {
          val image = LocalRes.HEART_16_PNG

          val stars = Range(0, item).to(LazyList).map { _ => new ImageView(image) }.toArray
          this.setGraphic(new HBox(stars: _*))
        }
      }
    })
  }
}