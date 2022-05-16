package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.{Controller, SimpleFxmlLoader}
import com.goexp.galgame.gui.view.game.explorer.ExplorerController
import com.goexp.ui.javafx.TaskService
import javafx.collections.ObservableList
import javafx.concurrent.Task
import scalafx.Includes._
import scalafx.scene.layout.StackPane

class ExplorerData(private val taskCreator: () => Task[ObservableList[Game]]) extends StackPane with Controller {

  private val loader = new SimpleFxmlLoader[ExplorerController]("explorer.fxml")
  private val node = loader.node
  private val controller = loader.controller

  private val queryService = new TaskService(taskCreator)

  init()

  override def load(): Unit = {
    queryService.restart()
  }

  private def init() = {

    queryService.value.onChange((_, _, newValue) => {
      if (newValue != null) {
        controller.load(newValue)
      }
    })

    controller.loadingBar.visible <== queryService.running

    registestListener(controller.loadingBar.visible)

    children += node
  }

}

object ExplorerData {
  def apply(taskCreator: => Task[ObservableList[Game]]): ExplorerData = new ExplorerData(() => taskCreator)
}