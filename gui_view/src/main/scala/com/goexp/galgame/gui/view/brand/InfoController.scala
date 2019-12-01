package com.goexp.galgame.gui.view.brand

import java.util.Objects

import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.game.search.ByBrand
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.game.listview.DataViewController
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML

class InfoController extends DefaultController {

  @FXML var titleController: TitlePartController = _
  @FXML var dataViewController: DataViewController = _

  private var brand = new Brand

  final private val gameByBrand = TaskService(new ByBrand(brand.id))

  override protected def initialize() = {
    gameByBrand.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) load(newValue)
    })

    dataViewController.progessloading.visibleProperty.bind(gameByBrand.runningProperty)

    dataViewController.reloadProperty.addListener((_, _, newValue) => {
      if (newValue) gameByBrand.restart()

    })
  }

  private def load(games: ObservableList[Game]) = {
    val filteredGames = new FilteredList[Game](games)
    dataViewController.load(filteredGames)
  }

  def load(brand: Brand) = {
    Objects.requireNonNull(brand)

    logger.info(s"Brand[${brand.id}] ${brand.name} state:<${brand.isLike}>")

    this.brand = brand
    titleController.init(brand)
    gameByBrand.restart()
  }
}