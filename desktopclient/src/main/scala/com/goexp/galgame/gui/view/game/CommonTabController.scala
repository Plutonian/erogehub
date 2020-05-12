package com.goexp.galgame.gui.view.game

import java.util.function.Predicate

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.view.game.listview.DataViewController
import com.goexp.ui.javafx.{FXMLLoaderProxy, TaskService}
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.scene.layout.Region

class CommonTabController(private val taskCreator: () => Task[ObservableList[Game]]) {
  val loader = new FXMLLoaderProxy[Region, DataViewController]("dataview.fxml")
  val node = loader.node
  val controller = loader.controller
  private val gameSearchService = new TaskService(taskCreator)

  init()


  private def init() = {

    controller.reloadProperty.addListener((_, _, newValue) => {
      if (newValue) gameSearchService.restart()

    })
    gameSearchService.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null)
        controller.load(newValue)
    })

    controller.progessloading.visibleProperty.bind(gameSearchService.runningProperty)
  }

  private var initPredicate: Predicate[Game] = _

  def load(initPredicate: Predicate[Game] = null): Unit = {
    this.initPredicate = initPredicate

    gameSearchService.restart()
  }


}

object CommonTabController {
  def apply(taskCreator: => Task[ObservableList[Game]]): CommonTabController = new CommonTabController(taskCreator _)
}