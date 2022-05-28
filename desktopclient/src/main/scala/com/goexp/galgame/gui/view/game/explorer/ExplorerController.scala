package com.goexp.galgame.gui.view.game.explorer

import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.BrandItem
import com.goexp.galgame.gui.util.Websites
import com.goexp.galgame.gui.view.game.explorer.sidebar.{BrandGroupView, DateGroupController, FilterCondition, FilterPanel}
import com.goexp.galgame.gui.view.game.explorer.tableview.TableListController
import com.goexp.ui.javafx.DefaultController
import javafx.beans.binding.Bindings
import javafx.collections.ObservableList
import javafx.collections.transformation.{FilteredList, SortedList}
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout.BorderPane
import org.bson.conversions.Bson
import org.controlsfx.control.PopOver
import scalafx.Includes._

import scala.jdk.CollectionConverters._


class ExplorerController extends DefaultController {

  final private val brandGroupView = new BrandGroupView()
  final private val popPanel = new PopOver
  /**
   * Controllers
   */
  @FXML var tablelistController: TableListController = _
  @FXML var loadingBar: ProgressBar = _

  @FXML private var filterPanel: BorderPane = _
  @FXML private var dateGroupController: DateGroupController = _
  /**
   * Status bar
   */
  @FXML private var lbItemCount: Label = _
  @FXML private var lbItemFullCount: Hyperlink = _
  /**
   * main panel
   */
  @FXML private var tablelist: TableView[Game] = _

  /**
   * Sidebar
   */
  @FXML private var brandGroup: TitledPane = _

  private var filteredGames: FilteredList[Game] = _
  //  private var groupPredicate: Predicate[Game] = _
  //  private var filterPredicate: Predicate[Game] = _

  //  private var groupFilter: Bson = _
  //  private var filterFilter: Bson = _

  final private val filterCondition = new FilterCondition()
  final private val filter = new FilterPanel()

  final var initFilter: Bson = _


  def load(games: ObservableList[Game]): Unit = {


    filteredGames = new FilteredList(games)
    // set defaultPredicate
    //    filterPredicate = FilterCondition.DEFAULT_GAME_PREDICATE

    filterPanel.setVisible(true)


    lbItemCount.text <== Bindings.size(filteredGames).asString()
    lbItemFullCount.text <== Bindings.size(games).asString()

    //    filterFilter = FilterCondition.FilterUtils.DEFAULT_GAME_FILTER
    //    groupFilter = null


    // set filter
    filteredGames.setPredicate(filterCondition.finalPredicate())


    val sortedData = new SortedList[Game](filteredGames)
    sortedData.comparator <== tablelist.comparator


    reload()
    loadTable(sortedData)
    loadGroupPanelData(filteredGames)
  }

  object Page {
    def openDetail(id: Int) = {
      filteredGames.asScala.find { g => g.id == id }.foreach(g => HGameApp.loadDetail(g))
    }

    def openBrand(id: Int) = {
      filteredGames.asScala.find { g => g.id == id }.foreach(g => HGameApp.viewBrand(g.brand))
    }

    def openUrl(url: String) = {
      Websites.open(url)
    }

  }

  def reload(): Unit = {
    tablelist.scrollTo(0)


  }

  private def loadGroupPanelData(filteredGames: FilteredList[Game]) = {
    dateGroupController.init(filteredGames)
    brandGroupView.init(filteredGames)

  }

  private def loadTable(sortedData: SortedList[Game]) = {
    tablelist.setItems(sortedData)
    tablelist.scrollTo(0)
  }

  override protected def initComponent() = {
    popPanel.setAutoHide(true)
    popPanel.setAnimated(false)

    brandGroup.setContent(brandGroupView)
    filterPanel.setCenter(filter)
  }

  override protected def eventBinding(): Unit = {
    initFilterPanel()
    initGroupPanel()


    lbItemFullCount.setOnAction(_ => {
      //      filterPredicate = FilterCondition.DEFAULT_GAME_PREDICATE
      //      groupPredicate = null

      //      filterFilter = FilterCondition.FilterUtils.DEFAULT_GAME_FILTER
      //      groupFilter = null

      // set filter
      filteredGames.setPredicate(filterCondition.finalPredicate())


      reload()

      loadGroupPanelData(filteredGames)
    })
  }

  private def initGroupPanel() = {

    dateGroupController.onSetProperty.onChange((_, _, newValue) => {
      if (newValue) {
        filterCondition.date = dateGroupController.selectedDate
        filterCondition.makeDatePredicate()
        filterCondition.FilterUtil.makeDateFilter()


        //        groupPredicate = filterCondition.groupPredicate

        filteredGames.setPredicate(filterCondition.finalPredicate())

        reload()
      }

    })


    brandGroupView.selectedBrand.onChange((_, _, newValue) => {
      if (newValue != null) {
        newValue.getValue match {
          case BrandItem(_, _, _) =>

            filterCondition.brand = newValue.getValue
            filterCondition.makeBrandPredicate()
            filterCondition.FilterUtil.makeBrandFilter()

            filteredGames.setPredicate(filterCondition.finalPredicate())
            reload()
          case _ =>
        }


      }

    })

  }

  private def initFilterPanel() = {

    // right sideBar
    filter.onSetProperty.addListener((_, _, newV) => {
      if (newV) {
        val load = filterCondition.groupPredicate == null


        filterCondition._selectedGameLocation = filter._selectedGameLocation
        filterCondition._selectedGameState = filter._selectedGameState
        filterCondition._selectedStar = filter._selectedStar
        filterCondition._switchAll = filter._switchAll.get()

        filterCondition.makeFilterPredicate()
        filterCondition.FilterUtil.makeFilterFilter()

        //        filterPredicate = filterCondition.filterPredicate


        filteredGames.setPredicate(filterCondition.finalPredicate())
        reload()


        if (load)
          loadGroupPanelData(filteredGames)
      }

    })

  }

}