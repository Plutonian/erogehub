package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.{Controller, SimpleFxmlLoader}
import com.goexp.galgame.gui.view.game.listview.DataViewController
import com.goexp.ui.javafx.TaskService
import javafx.collections.ObservableList
import javafx.concurrent.Task
import scalafx.scene.layout.StackPane
//import javafx.scene.layout.StackPane

class CommonDataViewPanel(private val taskCreator: () => Task[ObservableList[Game]]) extends StackPane with Controller {

  private val loader = new SimpleFxmlLoader[DataViewController]("dataview.fxml")
  private val node = loader.node
  private val controller = loader.controller

  private val queryService = new TaskService(taskCreator)

  init()

  private def init() = {

    queryService.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) {
        controller.load(newValue)
      }
    })

    controller.loadingBar.visibleProperty.bind(queryService.runningProperty)

    registestListener(controller.loadingBar.visibleProperty)

    children += node
  }

  override def load(): Unit = {
    queryService.restart()
  }

}

object CommonDataViewPanel {
  def apply(taskCreator: => Task[ObservableList[Game]]): CommonDataViewPanel = new CommonDataViewPanel(() => taskCreator)
}