package com.goexp.galgame.gui.view.brand

import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.game.search.ByBrand
import com.goexp.galgame.gui.view.game.explorer.ExplorerController
import com.goexp.ui.javafx.{DefaultController, TaskService}
import com.mongodb.client.model.Filters
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import scalafx.Includes._
import scalafx.beans.binding.Bindings

import java.util.Objects

class InfoController extends DefaultController {

  @FXML var titleController: TitlePartController = _
  @FXML var dataViewController: ExplorerController = _

  final lazy val brand = new SimpleObjectProperty[Brand]

  private val gameByBrand = TaskService(new ByBrand(brand.value.id))


  override protected def eventBinding(): Unit = {
    gameByBrand.value.onChange((_, _, newValue) => {
      if (newValue != null) {
        val filteredGames = new FilteredList[Game](newValue)

        dataViewController.load(filteredGames)
      }
    })


    brand.onChange((_, _, newValue) => {
      if (newValue != null) {

        logger.info(s"Brand[${newValue.id}] ${brand.name} state:<${newValue.state}>")

        titleController.brand = newValue
        titleController.compName <== Bindings.createStringBinding(() => newValue.comp)
        titleController.brandName <== Bindings.createStringBinding(() => newValue.name)
        titleController.state <==> newValue.state

        dataViewController.initFilter = Filters.eq("brandId", newValue.id)
        gameByBrand.restart()
      }

    })
  }
}