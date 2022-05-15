package com.goexp.galgame.gui.view.brand

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.brand.ChangeStateTask
import com.goexp.galgame.gui.task.brand.list.ByComp
import com.goexp.galgame.gui.task.game.change.MultiBlockByBrand
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.value.ChangeListener
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout.FlowPane
import javafx.scene.text.Text

import scala.jdk.CollectionConverters._

class TitlePartController extends DefaultController {

  @FXML private var txtComp: Text = _
  @FXML private var menuComp: MenuButton = _
  @FXML private var choiceBrandState: ChoiceBox[BrandState] = _
  @FXML private var btnBlock: Button = _

  private object VO {
    var changeBrand: Brand = _
  }

  import VO._

  final private val changeBrandStateService = TaskService(new ChangeStateTask(changeBrand))
  final private val changeGameStateService = TaskService(new MultiBlockByBrand(changeBrand.id))
  final private val listBrandService = TaskService(new ByComp(changeBrand.comp))


  private var listener: ChangeListener[BrandState] = _

  override protected def initComponent(): Unit = {
    val types =
      BrandState.values().to(LazyList)
        .filter {
          _ ne BrandState.ALL
        }
        .reverse
        .asJava

    choiceBrandState.setItems(FXCollections.observableArrayList(types))


    listener = (_, _, newValue) => {
      if (newValue != null) {

        logger.debug(s"<Action>Value:${choiceBrandState.getValue},New:${newValue}")

        changeBrand.state = newValue
        changeBrandStateService.restart()
        if (newValue eq BrandState.BLOCK)
          changeGameStateService.restart()
      }

    }

  }

  override protected def eventBinding(): Unit = {

    listBrandService.valueProperty.addListener { (_, _, newValue) =>
      if (newValue != null) {
        val items = newValue.to(LazyList)
          .filter { b => b != changeBrand }
          .map { brand =>
            val item = new MenuItem
            item.setText(brand.name)
            item.setUserData(brand)
            item.setOnAction(_ => HGameApp.viewBrand(brand))
            item
          }.asJava

        menuComp.getItems.setAll(FXCollections.observableArrayList(items))
      }
    }

    btnBlock.setOnAction { _ =>
      choiceBrandState.getSelectionModel.select(BrandState.BLOCK)
    }
  }

  def init(brand: Brand) = {
    choiceBrandState.setVisible(true)

    changeBrand = brand
    menuComp.setText(brand.name)

    if (Strings.isNotEmpty(brand.comp)) {
      txtComp.setText(brand.comp)
      listBrandService.restart()
    }
    //            menuComp.setVisible(false);
    //    boxWebsite.getChildren.clear()
    choiceBrandState.valueProperty.removeListener(listener)
    choiceBrandState.setValue(brand.state)
    choiceBrandState.valueProperty.addListener(listener)
  }

}