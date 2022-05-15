package com.goexp.galgame.gui.view.brand

import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search.ByBrand
import com.goexp.galgame.gui.view.game.explorer.ExplorerController
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import scalafx.Includes._
import scalafx.beans.binding.Bindings

import java.util.Objects

class InfoController extends DefaultController {

  @FXML var titleController: TitlePartController = _
  @FXML var dataViewController: ExplorerController = _

  private var brand = new Brand

  final private val gameByBrand = TaskService(new ByBrand(brand.id))


  override protected def eventBinding(): Unit = {
    gameByBrand.valueProperty.addListener((_, _, newValue) => {
      if (newValue != null) load(newValue)
    })
  }

  private def load(games: ObservableList[Game]) = {
    val filteredGames = new FilteredList[Game](games)
    dataViewController.load(filteredGames)
  }

  def load(brand: Brand) = {
    Objects.requireNonNull(brand)

    logger.info(s"Brand[${brand.id}] ${brand.name} state:<${brand.state}>")

    this.brand = brand

    titleController.brand = brand
    titleController.compName <== Bindings.createStringBinding(() => brand.comp)
    titleController.brandName <== Bindings.createStringBinding(() => brand.name)
    titleController.state <==> brand.state

    gameByBrand.restart()
  }
}