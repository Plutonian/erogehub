package com.goexp.galgame.gui.view.game.listview

import java.util
import java.util.function.Predicate

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.panel.group.node.DefaultItem
import com.goexp.galgame.gui.task.game.panel.group.{ByCV, ByTag}
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.game.listview.imglist.ImgListViewController
import com.goexp.galgame.gui.view.game.listview.sidebar.{BrandGroupController, DateGroupController, FilterPanelController}
import com.goexp.galgame.gui.view.game.listview.tableview.TableController
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.transformation.{FilteredList, SortedList}
import javafx.collections.{FXCollections, ObservableList}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout.{HBox, Region}

class DataViewController extends DefaultController {
  final val reloadProperty = new SimpleBooleanProperty(false)
  /**
    * Controllers
    */
  @FXML private var imgViewController: ImgListViewController = _
  @FXML var tableViewController: TableController = _
  @FXML private var filterPanelController: FilterPanelController = _
  @FXML private var brandGroupController: BrandGroupController = _
  @FXML private var dateGroupController: DateGroupController = _
  /**
    * Status bar
    */
  @FXML private var lbItemCount: Label = _
  @FXML var progessloading: ProgressBar = _
  /**
    * Toggle
    */
  @FXML private var gameViewChange: ToggleGroup = _
  //    @FXML
  //    private ToggleButton toggList;
  @FXML private var toggGrid: ToggleButton = _
  @FXML private var toggSmall: ToggleButton = _
  @FXML private var toggImg: ToggleButton = _
  /**
    * main panel
    */
  @FXML private var tableView: TableView[Game] = _
  @FXML private var smallListSimple: ListView[Game] = _
  @FXML private var imgView: Region = _
  /**
    * Sidebar
    */
  @FXML private var groupPanel: Region = _
  @FXML private var filterPanel: Region = _
  @FXML private var btnHide: Button = _
  @FXML private var cvList: ListView[DefaultItem] = _
  @FXML private var tagList: ListView[DefaultItem] = _
  private var filteredGames: FilteredList[Game] = _
  private var groupPredicate: Predicate[Game] = _

  final private val groupCVServ = TaskService(() => new ByCV(filteredGames))
  final private val groupTagServ = TaskService(() => new ByTag(filteredGames))

  override protected def initialize() = {
    initSwitchBar()
    initSideBar()
    initGroupPanel()
    btnHide.fire()
  }

  private def initGroupPanel() = {
    cvList.setCellFactory(_ => new ListCell[DefaultItem]() {
      override protected def updateItem(cvItemNode: DefaultItem, empty: Boolean) = {
        super.updateItem(cvItemNode, empty)
        setText(null)
        setGraphic(null)
        if (cvItemNode != null && !empty)
          setGraphic(new HBox(Tags.toNodes(cvItemNode.title), new Label(String.valueOf(cvItemNode.count))))
      }
    })
    tagList.setCellFactory(_ => new ListCell[DefaultItem]() {
      override protected def updateItem(tagNode: DefaultItem, empty: Boolean) = {
        super.updateItem(tagNode, empty)
        setText(null)
        setGraphic(null)
        if (tagNode != null && !empty) setGraphic(new HBox(Tags.toNodes(tagNode.title), new Label(String.valueOf(tagNode.count))))
      }
    })
    groupCVServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) cvList.setItems(FXCollections.observableList(newValue))
    })
    groupTagServ.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) tagList.setItems(FXCollections.observableList(newValue))
    })
  }

  private def initSwitchBar() = {
    toggGrid.setUserData(tableView)
    toggSmall.setUserData(smallListSimple)
    toggImg.setUserData(imgView)
    gameViewChange.selectedToggleProperty.addListener((_, oldValue, checkedBox) => {
      if (checkedBox != null) checkedBox.getUserData.asInstanceOf[Region].toFront()
      else gameViewChange.selectToggle(oldValue)
    })
  }

  private def initSideBar() = {
    btnHide.setUserData(true)
    btnHide.getProperties.put("groupPanel", groupPanel.getPrefWidth)
    btnHide.setOnAction(_ => {
      var state = btnHide.getUserData.asInstanceOf[Boolean]
      val width = btnHide.getProperties.get("groupPanel").asInstanceOf[Double]
      state = !state
      groupPanel.setVisible(state)
      groupPanel.setPrefWidth(if (state) width else 0)
      btnHide.setUserData(state)
    })
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
    tableView.scrollTo(0)
    smallListSimple.scrollTo(0)
    resetCount(filteredGames)
  }

  def load(games: ObservableList[Game], initPredicate: Predicate[Game]): Unit = {
    filterPanel.setVisible(true)
    groupPredicate = null
    filteredGames = new FilteredList(games)

    // set filter
    filteredGames.setPredicate(initPredicate)
    recount()

    // set defaultPredicate
    filterPanelController.predicate = initPredicate
    val sortedData = new SortedList[Game](filteredGames)
    sortedData.comparatorProperty.bind(tableView.comparatorProperty)
    loadItems(sortedData)
    setSideBarData(filteredGames)
  }

  def load(games: ObservableList[Game]): Unit = {
    val defaultP: Predicate[Game] = (g: Game) => (g.state.get ne GameState.SAME) &&
      (g.state.get ne GameState.BLOCK) &&
      !(g.star > 0 &&
        g.star < 3)

    load(games, defaultP)
  }

  private def setSideBarData(filteredGames: FilteredList[Game]) = {
    dateGroupController.init(filteredGames)
    brandGroupController.init(filteredGames)
    groupCVServ.restart()
    groupTagServ.restart()
  }

  private def loadItems(sortedData: SortedList[Game]) = {
    tableView.setItems(sortedData)
    tableView.scrollTo(0)
    smallListSimple.setItems(sortedData)
    smallListSimple.scrollTo(0)
  }

  private def resetCount(filteredGames: util.List[Game]) =
    lbItemCount.setText(s"${filteredGames.size} 件")

  @FXML private def reload_OnAction(event: ActionEvent) = {
    reloadProperty.set(true)
    reloadProperty.set(false)
  }
}