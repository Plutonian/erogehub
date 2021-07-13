package com.goexp.galgame.gui.view.game.listview

import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{DataItem, SampleItem}
import com.goexp.galgame.gui.task.game.panel.group.{ByCV, ByTag}
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.game.detailview.outer.OutPageController
import com.goexp.galgame.gui.view.game.listview.sidebar.{BrandGroupController, DateGroupController, FilterPanelController}
import com.goexp.galgame.gui.view.game.listview.simplelist.small.{HeaderController, ListViewController}
import com.goexp.galgame.gui.view.game.listview.tablelist.TableListController
import com.goexp.ui.javafx.control.cell.NodeListCell
import com.goexp.ui.javafx.{DefaultController, FXMLLoaderProxy, TaskService}
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.transformation.{FilteredList, SortedList}
import javafx.collections.{FXCollections, ObservableList}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.{FlowPane, HBox, Region}
import org.controlsfx.control.PopOver

import java.util
import java.util.function.Predicate
import scala.jdk.CollectionConverters._

class DataViewController extends DefaultController {
  final val reloadProperty = new SimpleBooleanProperty(false)
  /**
   * Controllers
   */
  @FXML var tablelistController: TableListController = _
  @FXML private var filterPanelController: FilterPanelController = _
  @FXML private var brandGroupController: BrandGroupController = _
  @FXML private var dateGroupController: DateGroupController = _
  @FXML private var smallListSimpleController: ListViewController = _
  /**
   * Status bar
   */
  @FXML private var lbItemCount: Label = _
  @FXML var progessloading: ProgressBar = _
  /**
   * main panel
   */
  //  @FXML private var tableView: TableView[Game] = _
  @FXML private var tablelist: TableView[Game] = _
  @FXML private var mainTab: TabPane = _
  /**
   * Sidebar
   */
  @FXML private var groupPanel: Accordion = _
  @FXML private var filterPanel: Region = _
  @FXML private var btnHide: Button = _
  @FXML private var cvList: ListView[DataItem] = _
  @FXML private var tagList: ListView[DataItem] = _

  @FXML private var gridView: FlowPane = _
  private val popPanel = new PopOver

  private var filteredGames: FilteredList[Game] = _
  private var groupPredicate: Predicate[Game] = _

  final private val groupCVServ = TaskService(new ByCV(filteredGames))
  final private val groupTagServ = TaskService(new ByTag(filteredGames))

  override protected def initialize() = {
    //    val tabs = mainTab.getTabs
    //    val lastTab = tabs.get(tabs.size() - 1)
    mainTab.getSelectionModel.selectLast()

    initSideBar()
    initGroupPanel()
    btnHide.fire()

    //    popPanel.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT)
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

    groupPanel.setExpandedPane(groupPanel.getPanes.get(0))


    btnHide.setUserData(true)
    btnHide.getProperties.put("groupPanel", groupPanel.getPrefWidth)
    btnHide.setOnAction { _ =>
      var state = btnHide.getUserData.asInstanceOf[Boolean]
      val width = btnHide.getProperties.get("groupPanel").asInstanceOf[Double]
      state = !state
      groupPanel.setVisible(state)
      groupPanel.setManaged(state)
      groupPanel.setPrefWidth(if (state) width else 0)
      btnHide.setUserData(state)
    }
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
    smallListSimpleController.smallListSimple.scrollTo(0)
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

  //  def load(games: ObservableList[Game]): Unit = {
  //    val defaultP: Predicate[Game] = (g: Game) => (g.state.get ne GameState.SAME) &&
  //      (g.state.get ne GameState.BLOCK)
  //
  //    load(games, defaultP)
  //  }

  private def setSideBarData(filteredGames: FilteredList[Game]) = {
    dateGroupController.init(filteredGames)
    brandGroupController.init(filteredGames)
    groupCVServ.restart()
    groupTagServ.restart()
  }

  private def loadItems(sortedData: SortedList[Game]) = {
    //    tableView.setItems(sortedData)
    //    tableView.scrollTo(0)
    tablelist.setItems(sortedData)
    tablelist.scrollTo(0)
    smallListSimpleController.smallListSimple.setItems(sortedData)
    smallListSimpleController.smallListSimple.scrollTo(0)

    loadFlow(sortedData)
  }

  private def resetCount(filteredGames: util.List[Game]) = {

    lbItemCount.setText(s"${filteredGames.size} 件")
    loadFlow(filteredGames)

  }

  private def loadFlow(filteredGames: util.List[Game]) = {


    gridView.getChildren.clear()

    val iamges = filteredGames.listIterator().asScala.map { game =>
      val imageView = new ImageView(GameImage(game).tiny200())
      imageView.setOnMouseClicked { e =>
        if (e.getButton eq MouseButton.PRIMARY) {

          val loader = new FXMLLoaderProxy[Region, HeaderController]("header.fxml")
          //          val loader = new FXMLLoaderProxy[Region, OutPageController]("out_page.fxml")
          loader.controller.load(game)

          //        if (!popPanel.isShowing)
          popPanel.show(imageView)

          popPanel.setContentNode(loader.node)
        }


      }
      //      imageView.setOnMouseExited { _ =>
      //        if (popPanel.isShowing)
      //          popPanel.hide()
      //      }
      imageView

    }.toArray
    gridView.getChildren.addAll(iamges: _*)

  }

  @FXML private def reload_OnAction(event: ActionEvent) = {
    reloadProperty.set(true)
    reloadProperty.set(false)
  }
}