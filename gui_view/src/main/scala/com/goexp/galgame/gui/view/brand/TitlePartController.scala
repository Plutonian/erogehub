package com.goexp.galgame.gui.view.brand

import java.util

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.brand.ChangeIsLikeTask
import com.goexp.galgame.gui.task.brand.list.ByComp
import com.goexp.galgame.gui.task.game.change.MultiLikeByBrand
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.game.HomeController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.FXML
import javafx.scene.control.{ChoiceBox, Label, MenuButton, MenuItem}
import javafx.scene.image.ImageView
import javafx.scene.layout.{FlowPane, HBox}
import javafx.scene.text.Text
import javafx.util.StringConverter

import scala.jdk.CollectionConverters._

class TitlePartController extends DefaultController {
  final val stateChangeProperty = new SimpleBooleanProperty(false)
  @FXML private var txtComp: Text = _
  @FXML private var menuComp: MenuButton = _
  @FXML private var boxWebsite: HBox = _
  @FXML private var imageFav: ImageView = _
  @FXML private var tagPanel: FlowPane = _
  @FXML private var choiceBrandState: ChoiceBox[BrandType] = _
  private var changeBrand: Brand = _
  final private val changeBrandStateService = new TaskService[Boolean](() => new ChangeIsLikeTask(changeBrand))
  final private val changeGameStateService = new TaskService[Unit](() => new MultiLikeByBrand(changeBrand.id))
  final private val listBrandService = new TaskService[util.List[Brand]](() => new ByComp(changeBrand.comp))
  private var listener: ChangeListener[BrandType] = _

  override protected def initialize() = {

    val types =
      BrandType.values().toList.to(LazyList)
        .filter { t: BrandType => t ne BrandType.ALL }
        .reverse
        .asJava

    choiceBrandState.setItems(FXCollections.observableArrayList(types))

    choiceBrandState.setConverter(new StringConverter[BrandType]() {
      override def toString(brandType: BrandType) = brandType.name

      override def fromString(string: String) = BrandType.from(string)
    })

    listener = (_, _, newValue) => {
      if (newValue != null) {
        logger.debug("<Action>Value:{},New:{}", choiceBrandState.getValue, newValue)
        changeBrand.setIsLike(newValue)
        changeBrandStateService.restart()
        if (newValue eq BrandType.BLOCK)
          changeGameStateService.restart()
      }

    }

    changeBrandStateService.valueProperty.addListener((_, _, newV) => {
      if (!newV) stateChangeProperty.set(true)

    })
    listBrandService.valueProperty.addListener((_, _, newValue) => {

      if (newValue != null) {
        val items = newValue.asScala.to(LazyList)
          .map { brand =>
            val item = new MenuItem
            item.setText(brand.name)
            item.setUserData(brand)
            item.setOnAction(_ => HomeController.$this.viewBrand(brand))
            item
          }.asJava

        menuComp.getItems.setAll(FXCollections.observableArrayList(items))
      }

    })

  }

  def init(brand: Brand) = {
    choiceBrandState.setVisible(true)
    stateChangeProperty.set(false)
    changeBrand = brand
    menuComp.setText(brand.name)

    if (Strings.isNotEmpty(brand.comp)) {
      txtComp.setText(brand.comp)
      listBrandService.restart()
    }
    //            menuComp.setVisible(false);
    boxWebsite.getChildren.clear()
    choiceBrandState.valueProperty.removeListener(listener)
    choiceBrandState.setValue(brand.isLike)
    choiceBrandState.valueProperty.addListener(listener)
  }

  def initTag(games: ObservableList[Game]) =

    Option(games.asScala.to(LazyList)).foreach {
      list => {
        val tagLbs = list
          .flatMap {
            _.tag.asScala
          }
          .filter(_.trim.length > 0)
          .groupBy {
            s => s
          }
          .to(LazyList)
          .sortBy { case (_, v) => v.size }.reverse
          .take(10)
          .map { case (k, _) =>
            val lb = new Label
            lb.setText(k)
            lb.getStyleClass.add("tag")
            lb

          }
          .asJava


        tagLbs.add(new Label("..."))
        tagPanel.getChildren.setAll(tagLbs)
      }
    }

}