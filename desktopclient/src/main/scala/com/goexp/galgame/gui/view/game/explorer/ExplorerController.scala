package com.goexp.galgame.gui.view.game.explorer

import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{DataItem, SampleItem}
import com.goexp.galgame.gui.task.game.panel.group.{ByCV, ByTag}
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.view.game.explorer.gridview.GameDetailView
import com.goexp.galgame.gui.view.game.explorer.sidebar.{BrandGroupView, DateGroupController, FilterPanel}
import com.goexp.galgame.gui.view.game.explorer.tableview.TableListController
import com.goexp.ui.javafx.control.cell.NodeListCell
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.collections.transformation.{FilteredList, SortedList}
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.FXML
import javafx.scene.control._
import org.controlsfx.control.{GridCell, GridView, PopOver}
import scalafx.Includes._
import scalafx.beans.property.StringProperty
import scalafx.scene.layout.HBox

import java.util
import java.util.function.Predicate
import scala.jdk.CollectionConverters._

class ExplorerController extends DefaultController {
  final private val groupCVServ = TaskService(new ByCV(filteredGames))
  final private val groupTagServ = TaskService(new ByTag(filteredGames))
  val view = new BrandGroupView()
  private val popPanel = new PopOver
  /**
   * Controllers
   */
  @FXML var tablelistController: TableListController = _
  @FXML var loadingBar: ProgressBar = _
  @FXML private var filterPanel: TitledPane = _
  @FXML private var dateGroupController: DateGroupController = _
  /**
   * Status bar
   */
  @FXML private var lbItemCount: Label = _
  /**
   * main panel
   */
  @FXML private var tablelist: TableView[Game] = _

  /**
   * Sidebar
   */
  @FXML private var cvList: ListView[DataItem] = _
  @FXML private var tagList: ListView[DataItem] = _
  @FXML private var brandGroup: TitledPane = _
  @FXML private var gridView: GridView[Game] = _
  private var filteredGames: FilteredList[Game] = _
  private var groupPredicate: Predicate[Game] = _

  val panel = new FilterPanel()

  def load(games: ObservableList[Game], initPredicate: Predicate[Game] = null): Unit = {
    filterPanel.setVisible(true)
    groupPredicate = null
    filteredGames = new FilteredList(games)


    val initP = HGameApp.mergeP(initPredicate)
    // set filter
    filteredGames.setPredicate(initP)
    recount()

    // set defaultPredicate
    panel.predicate = initP

    val sortedData = new SortedList[Game](filteredGames)
    sortedData.comparatorProperty.bind(tablelist.comparatorProperty)
    loadItems(sortedData)
    setSideBarData(filteredGames)
  }

  def recount(): Unit = {
    tablelist.scrollTo(0)
    resetCount(filteredGames)
  }

  private def resetCount(filteredGames: util.List[Game]) = {
    lbItemCount.setText(s"${filteredGames.size} 件")
  }

  private def setSideBarData(filteredGames: FilteredList[Game]) = {
    dateGroupController.init(filteredGames)
    view.init(filteredGames)
    groupCVServ.restart()
    groupTagServ.restart()
  }

  private def loadItems(sortedData: SortedList[Game]) = {
    tablelist.setItems(sortedData)
    tablelist.scrollTo(0)

    gridView.setItems(sortedData)

  }

  override protected def initialize() = {

    initSideBar()
    initGroupPanel()

    gridView.setCellWidth(300)
    gridView.setCellHeight(600)

    gridView.setHorizontalCellSpacing(5)
    gridView.setVerticalCellSpacing(5)

    gridView.setCellFactory { _ =>

      val view = new GameDetailView()

      new GridCell[Game] {
        itemProperty().addListener { (_, _, g) => {
          setGraphic({
            if (g != null) {
              view.load(g)
              view
            } else
              null
          })
        }
        }
      }

    }

    popPanel.setAutoHide(true)
    popPanel.setAnimated(false)


  }

  private def initGroupPanel() = {
    cvList.setCellFactory(_ =>


      new ListCell[DataItem] {

        import VO._

        object VO {
          val _title = new StringProperty()
          val _count = new StringProperty()
        }

        val c = new HBox {
          children ++= Seq(
            new Label {
              textProperty() <== _title
            },
            new Label {
              textProperty <== _count
            }
          )
        }

        itemProperty().onChange { (_, _, item) =>
          if (item != null) {

            val SampleItem(title, count) = item
            _title.value = title
            _count.value = s"($count)"

            setGraphic(c)
          }
          else {
            //            _title.value = ""
            //            _count.value = ""

            setGraphic(null)
          }

        }
      }

    )

    cvList.getSelectionModel.selectedItemProperty().addListener((_, _, cv) => {
      cv match {
        case SampleItem(title, _) =>
          val defaultP: Predicate[Game] = (g: Game) => Option(g.gameCharacters).exists(_.asScala.exists(_.getShowCV().exists(_ == title)))

          groupPredicate = defaultP
          val filterPredicate = panel.predicate
          val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
          else groupPredicate
          filteredGames.setPredicate(p)
          recount()
        case _ =>
      }

    })

    tagList.setCellFactory(_ =>
      new ListCell[DataItem] {

        import VO._

        object VO {
          val _title = new StringProperty()
          val _count = new StringProperty()
        }

        val c = new HBox {
          children ++= Seq(
            new Label {
              textProperty() <== _title
              getStyleClass.add("tag")
            },
            new Label {
              textProperty <== _count
            }
          )
        }

        itemProperty().onChange { (_, _, item) =>
          if (item != null) {

            val SampleItem(title, count) = item
            _title.value = title
            _count.value = s"($count)"

            setGraphic(c)
          }
          else {
            //            _title.value = ""
            //            _count.value = ""

            setGraphic(null)
          }

        }
      }
    )

    groupCVServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {
        cvList.getSelectionModel.clearSelection()
        cvList.setItems(FXCollections.observableList(newValue))
      }
    })
    groupTagServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) tagList.setItems(FXCollections.observableList(newValue))
    })
  }

  private def initSideBar() = {

    initSidebarContentView()
  }

  private def initSidebarContentView() = {
    filterPanel.setContent(panel)

    panel.onSetProperty.addListener((_, _, newV) => {
      if (newV) {
        val load = groupPredicate == null
        val filterPredicate = panel.predicate
        val p = if (groupPredicate != null) filterPredicate.and(groupPredicate)
        else filterPredicate
        filteredGames.setPredicate(p)
        recount()
        if (load) setSideBarData(filteredGames)
      }

    })


    dateGroupController.onSetProperty.addListener((_, _, newValue) => {
      if (newValue) {
        groupPredicate = dateGroupController.predicate
        val filterPredicate = panel.predicate
        val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
        else groupPredicate
        filteredGames.setPredicate(p)
        recount()
      }

    })


    view.onSetProperty.onChange((_, _, newValue) => {
      if (newValue) {
        groupPredicate = view.predicate
        val filterPredicate = panel.predicate
        val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
        else groupPredicate
        filteredGames.setPredicate(p)
        recount()
      }

    })
    brandGroup.setContent(view)
  }

}