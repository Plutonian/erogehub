package com.goexp.galgame.gui.view.game

import java.util.function.Predicate

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.util.FXMLLoaderProxy
import com.goexp.galgame.gui.view.game.listview.DataViewController
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.scene.layout.Region

class CommonTabController(private[this] val taskCreator: () => Task[ObservableList[Game]]) {
  val loader = new FXMLLoaderProxy[Region, DataViewController](classOf[DataViewController].getResource("dataview.fxml"))
  val node = loader.node
  val controller = loader.controller
  private val gameSearchService = TaskService(taskCreator)

  init()


  private def init() = {

    controller.reloadProperty.addListener((_, _, newValue) => {
      if (newValue) gameSearchService.restart()

    })
    gameSearchService.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null)
        if (initPredicate != null) controller.load(newValue, initPredicate)
        else controller.load(newValue)
    })

    controller.progessloading.visibleProperty.bind(gameSearchService.runningProperty)
  }

  private var initPredicate: Predicate[Game] = _

  def load(initPredicate: Predicate[Game]): Unit = {
    this.initPredicate = initPredicate
    load()
  }

  def load(): Unit = gameSearchService.restart()
}