package com.goexp.galgame.gui.view.game.explorer.tableview

import com.goexp.galgame.common.model.game.{GameLocation, GameState}
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.change.{MultiBlock, MultiLocation, MultiState}
import com.goexp.galgame.gui.util.SimpleFxmlLoader
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.common.control.FormattedDate
import com.goexp.ui.javafx.control.cell.{NodeTableCell, TableCell, TextTableCell}
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView

import java.time.LocalDate
import java.util
import scala.jdk.CollectionConverters._

class TableListController extends DefaultController {
  @FXML var table: TableView[Game] = _

  @FXML var tableColDate: TableColumn[Game, LocalDate] = _
  @FXML var tableColImg: TableColumn[Game, Game] = _
  @FXML var tableColTitle: TableColumn[Game, String] = _
  @FXML var tableColType: TableColumn[Game, String] = _
  @FXML var tableColPainter: TableColumn[Game, String] = _
  @FXML var tableColWriter: TableColumn[Game, String] = _
  @FXML var tableColState: TableColumn[Game, GameState] = _
  @FXML var tableColLocation: TableColumn[Game, GameLocation] = _

  @FXML private var menuState: Menu = _
  @FXML private var menuLocation: Menu = _

  private var selectedGames: util.List[Game] = _
  final private val changeGameStateService = TaskService(new MultiState(selectedGames))
  final private val blockGameService = TaskService(new MultiBlock(selectedGames))
  final private val changeGameLocationService = TaskService(new MultiLocation(selectedGames))

  override protected def initialize() = {

    def initMenuState() = {
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

              if (gameState == GameState.BLOCK) {

                selectedGames.forEach { g =>
                  g.star.set(0)
                  g.location.set(GameLocation.REMOTE)
                }

                blockGameService.restart()
              } else {
                changeGameStateService.restart()
              }
            })
            menuItem
          }).asJava

      menuState.getItems.setAll(items)
    }

    def initMenuLocation() = {
      val items =
        GameLocation.values.to(LazyList).reverse
          .map(loc => {
            val menuItem = new MenuItem
            menuItem.setUserData(loc)
            menuItem.setText(loc.name)
            menuItem.setOnAction(e => {
              val gameLoc = ((e.getSource).asInstanceOf[MenuItem]).getUserData.asInstanceOf[GameLocation]

              logger.debug(s"<MenuItem>:${gameLoc.name}")

              selectedGames = table.getSelectionModel.getSelectedItems
              selectedGames.forEach(_.location.set(gameLoc))
              changeGameLocationService.restart()
            })
            menuItem
          }).asJava

      menuLocation.getItems.setAll(items)
    }


    initMenuState()
    initMenuLocation()


    table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)

    tableColLocation.setCellValueFactory(param => param.getValue.location)
    tableColState.setCellValueFactory(param => param.getValue.state)
    tableColImg.setCellValueFactory(param => new SimpleObjectProperty(param.getValue))
//    tableColPainter.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.getPainter))
    tableColWriter.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.getWriter))
    tableColDate.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.publishDate))
    tableColTitle.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.name))
    tableColType.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.getType))


    tableColDate.setPrefWidth(100)
    tableColDate.setCellFactory { _ =>

      val dateShow = new FormattedDate()

      NodeTableCell { publishDate =>

        dateShow.date(publishDate)
        dateShow
      }

    }


    //    tableColTitle.setCellFactory(_ =>
    //      TextTableCell { name =>
    //        name.replaceAll("＜[^＞]*＞", "")
    //      }
    //    )

    tableColTitle.setPrefWidth(900)

    tableColTitle.setCellFactory(_ => {

      val loader = new SimpleFxmlLoader[InfoController]("info.fxml")

      new TableCell[Game, String]() {

        override protected def notEmpty(gameState: String): Unit = {

          Option(this.getTableRow.getItem).foreach { game =>
            loader.controller.load(game)
            this.setGraphic(loader.node)
          }
        }

      }
    })


    tableColLocation.setCellFactory(_ =>
      TextTableCell { location =>
        location.name
      })

    tableColState.setCellFactory(_ => new TableCell[Game, GameState]() {

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


    tableColImg.setCellFactory(_ =>


      NodeTableCell { game =>
        new ImageView({
          if (game.isOkImg) GameImage(game).tiny() else null
        })
      }
    )
  }
}