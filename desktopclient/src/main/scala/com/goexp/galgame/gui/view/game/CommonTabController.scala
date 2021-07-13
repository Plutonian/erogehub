package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.SimpleFxmlLoader
import com.goexp.galgame.gui.view.game.listview.DataViewController
import com.goexp.ui.javafx.TaskService
import javafx.collections.ObservableList
import javafx.concurrent.Task

class CommonTabController(private val taskCreator: () => Task[ObservableList[Game]]) {

  private val loader = new SimpleFxmlLoader[DataViewController]("dataview.fxml")
  val node = loader.node
  val controller = loader.controller

  private val queryService = new TaskService(taskCreator)

  init()

  private def init() = {

    queryService.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {
        controller.load(newValue)
        //        controller.loadingBar.setVisible(false)
      }
    })

    //    controller.loadingBar.visibleProperty.bind(queryService.runningProperty)
  }

  def load(): Unit = {
    queryService.restart()
    //    controller.loadingBar.setVisible(true)
  }


}

object CommonTabController {
  def apply(taskCreator: => Task[ObservableList[Game]]): CommonTabController = new CommonTabController(() => taskCreator)
}