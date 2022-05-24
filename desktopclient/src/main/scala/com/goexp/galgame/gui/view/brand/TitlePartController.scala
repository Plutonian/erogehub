package com.goexp.galgame.gui.view.brand

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.task.brand.ChangeStateTask
import com.goexp.galgame.gui.task.brand.list.ByComp
import com.goexp.galgame.gui.task.game.change.MultiBlockByBrand
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.text.Text
import scalafx.Includes._

import scala.jdk.CollectionConverters._

class TitlePartController extends DefaultController {

  @FXML private var txtComp: Text = _
  @FXML private var menuComp: MenuButton = _
  @FXML private var choiceBrandState: ChoiceBox[BrandState] = _
  @FXML private var btnBlock: Button = _

  var brand: Brand = _

  /**
   * VO
   */
  final val compName = new SimpleStringProperty
  final val brandName = new SimpleStringProperty
  final val state = new SimpleObjectProperty[BrandState]


  /**
   * Service
   */
  final private val changeBrandStateService = TaskService(new ChangeStateTask(brand))
  final private val changeGameStateService = TaskService(new MultiBlockByBrand(brand.id))
  final private val listBrandService = TaskService(new ByComp(brand.comp))

  override protected def initComponent(): Unit = {
    val types =
      BrandState.values().to(LazyList)
        .filter {
          _ ne BrandState.ALL
        }
        .reverse
        .asJava

    choiceBrandState.setItems(FXCollections.observableArrayList(types))

  }

  override protected def eventBinding(): Unit = {

    listBrandService.value.onChange { (_, _, newValue) =>
      if (newValue != null) {
        val items = newValue.to(LazyList)
          .filter { b => b != brand }
          .sortBy(_.state.get().value).reverse
          .map { brand =>
            val item = new MenuItem
            item.setText(s"${brand.name} [${brand.state.get()}]")
            item.setUserData(brand)
            item.setOnAction(_ => HGameApp.viewBrand(brand))
            item
          }.asJava

        menuComp.getItems.setAll(FXCollections.observableArrayList(items))
      }
    }

    btnBlock.setOnAction { _ =>
      state.set(BrandState.BLOCK)
    }


    state.onChange((_, oldValue, newValue) => {
      if (oldValue != null && newValue != null) {

        logger.debug(s"<Action>Old:${oldValue},New:${newValue}")

        changeBrandStateService.restart()
        if (newValue eq BrandState.BLOCK)
          changeGameStateService.restart()
      }
    })

    compName.onChange((_, _, newValue) => {
      if (Strings.isNotEmpty(newValue)) {
        listBrandService.restart()
      }
    })
  }

  override protected def dataBinding(): Unit = {
    choiceBrandState.value <==> state
    menuComp.text <== brandName
    txtComp.text <== compName
  }

}