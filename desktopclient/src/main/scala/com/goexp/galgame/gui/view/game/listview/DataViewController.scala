package com.goexp.galgame.gui.view.game.listview

import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{DataItem, SampleItem}
import com.goexp.galgame.gui.task.game.panel.group.{ByCV, ByTag}
import com.goexp.galgame.gui.util.{SimpleFxmlLoader, Tags}
import com.goexp.galgame.gui.view.game.listview.sidebar.{BrandGroupController, DateGroupController, FilterPanelController}
import com.goexp.galgame.gui.view.game.listview.simplelist.small.HeaderController
import com.goexp.galgame.gui.view.game.listview.tablelist.TableListController
import com.goexp.ui.javafx.control.cell.NodeListCell
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.collections.transformation.{FilteredList, SortedList}
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout.{HBox, Region}
import org.controlsfx.control.{GridCell, GridView, PopOver}

import java.util
import java.util.function.Predicate
import scala.jdk.CollectionConverters._

class DataViewController extends DefaultController {
  /**
   * Controllers
   */
  @FXML var tablelistController: TableListController = _
  @FXML private var filterPanelController: FilterPanelController = _
  @FXML private var brandGroupController: BrandGroupController = _
  @FXML private var dateGroupController: DateGroupController = _
  /**
   * Status bar
   */
  @FXML private var lbItemCount: Label = _
  @FXML var loadingBar: ProgressBar = _
  /**
   * main panel
   */
  @FXML private var tablelist: TableView[Game] = _
  //  @FXML private var mainTab: TabPane = _
  /**
   * Sidebar
   */
  @FXML private var filterPanel: Region = _
  @FXML private var cvList: ListView[DataItem] = _
  @FXML private var tagList: ListView[DataItem] = _

  @FXML private var gridView: GridView[Game] = _
  private val popPanel = new PopOver

  private var filteredGames: FilteredList[Game] = _
  private var groupPredicate: Predicate[Game] = _

  final private val groupCVServ = TaskService(new ByCV(filteredGames))
  final private val groupTagServ = TaskService(new ByTag(filteredGames))

  override protected def initialize() = {

    initSideBar()
    initGroupPanel()

    gridView.setCellWidth(300)
    gridView.setCellHeight(600)

    gridView.setCellFactory { _ =>
      val loader = new SimpleFxmlLoader[HeaderController]("header.fxml")

      new GridCell[Game] {
        itemProperty().addListener { (_, _, g) => {
          setGraphic({
            if (g != null) {
              loader.controller.load(g)
              loader.node
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
      NodeListCell[DataItem] {
        case SampleItem(title, count) =>
          new HBox(Tags.toNodes(title), new Label(String.valueOf(count)))
      }
    )

    cvList.getSelectionModel.selectedItemProperty().addListener((_, _, cv) => {
      cv match {
        case SampleItem(title, _) =>
          val defaultP: Predicate[Game] = (g: Game) => Option(g.gameCharacters).exists(_.asScala.exists(_.getShowCV().exists(_ == title)))

          groupPredicate = defaultP
          val filterPredicate = filterPanelController.predicate
          val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
          else groupPredicate
          filteredGames.setPredicate(p)
          recount()
        case _ =>
      }

    })

    tagList.setCellFactory(_ =>
      NodeListCell[DataItem] {
        case SampleItem(title, count) =>
          new HBox(Tags.toNodes(title), new Label(String.valueOf(count)))
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
    filterPanelController.onSetProperty.addListener((_, _, newV) => {
      if (newV) {
        val load = groupPredicate == null
        val filterPredicate = filterPanelController.predicate
        val p = if (groupPredicate != null) filterPredicate.and(groupPredicate)
        else filterPredicate
        filteredGames.setPredicate(p)
        recount()
        if (load) setSideBarData(filteredGames)
      }

    })
    brandGroupController.onSetProperty.addListener((_, _, newValue) => {
      if (newValue) {
        groupPredicate = brandGroupController.predicate
        val filterPredicate = filterPanelController.predicate
        val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
        else groupPredicate
        filteredGames.setPredicate(p)
        recount()
      }

    })
    dateGroupController.onSetProperty.addListener((_, _, newValue) => {
      if (newValue) {
        groupPredicate = dateGroupController.predicate
        val filterPredicate = filterPanelController.predicate
        val p = if (filterPredicate != null) groupPredicate.and(filterPredicate)
        else groupPredicate
        filteredGames.setPredicate(p)
        recount()
      }

    })
  }

  def recount(): Unit = {
    tablelist.scrollTo(0)
    resetCount(filteredGames)
  }

  def load(games: ObservableList[Game], initPredicate: Predicate[Game] = null): Unit = {
    filterPanel.setVisible(true)
    groupPredicate = null
    filteredGames = new FilteredList(games)


    val initP = HGameApp.mergeP(initPredicate)
    // set filter
    filteredGames.setPredicate(initP)
    recount()

    // set defaultPredicate
    filterPanelController.predicate = initP
    val sortedData = new SortedList[Game](filteredGames)
    sortedData.comparatorProperty.bind(tablelist.comparatorProperty)
    loadItems(sortedData)
    setSideBarData(filteredGames)
  }


  private def setSideBarData(filteredGames: FilteredList[Game]) = {
    dateGroupController.init(filteredGames)
    brandGroupController.init(filteredGames)
    groupCVServ.restart()
    groupTagServ.restart()
  }

  private def loadItems(sortedData: SortedList[Game]) = {
    tablelist.setItems(sortedData)
    tablelist.scrollTo(0)

    gridView.setItems(sortedData)

    //    loadFlow(sortedData)
  }

  private def resetCount(filteredGames: util.List[Game]) = {

    lbItemCount.setText(s"${filteredGames.size} 件")
    //    loadFlow(filteredGames)

  }

  //  private def loadFlow(filteredGames: util.List[Game]) = {
  //
  //    val iamges = filteredGames.listIterator().asScala.to(LazyList).map { game =>
  //
  //
  //
  //    }.asJava
  //
  //    //    gridView.setItems(filteredGames)
  //
  //  }

}