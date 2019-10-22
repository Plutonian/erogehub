package com.goexp.galgame.gui.view.brand

import java.time.LocalDate
import java.util

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.BrandType
import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.brand.search.{ByComp, ByName, ByType}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{TabSelect, Websites}
import com.goexp.galgame.gui.view.DefaultController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import javafx.util.StringConverter


class MainPanelController extends DefaultController {

  final val onLoadProperty = new SimpleBooleanProperty(false)
  var targetBrand: Brand = _
  @FXML var colStart: TableColumn[Brand, LocalDate] = _
  @FXML var colEnd: TableColumn[Brand, LocalDate] = _
  @FXML var colSize: TableColumn[Brand, Integer] = _
  @FXML private var textBrandKey: TextField = _
  @FXML private var tableBrand: TableView[Brand] = _
  @FXML private var colComp: TableColumn[Brand, String] = _
  @FXML private var colName: TableColumn[Brand, String] = _
  @FXML private var colWebsite: TableColumn[Brand, String] = _
  @FXML private var colState: TableColumn[Brand, BrandType] = _
  @FXML private var colCommand: TableColumn[Brand, Void] = _
  @FXML private var choiceBrandType: ChoiceBox[BrandType] = _
  @FXML private var typeGroup: ToggleGroup = _

  private var brandType = BrandType.LIKE
  private var keyword: String = _

  final private val brandService = TaskService(() => new ByType(brandType))
  final private val brandByNameService = TaskService(() => new ByName(keyword))
  final private val brandByCompService = TaskService(() => new ByComp(keyword))

  override protected def initialize() = {
    colComp.setCellValueFactory(new PropertyValueFactory[Brand, String]("comp"))
    colName.setCellValueFactory(new PropertyValueFactory[Brand, String]("name"))
    colWebsite.setCellValueFactory(new PropertyValueFactory[Brand, String]("website"))
    colState.setCellValueFactory(new PropertyValueFactory[Brand, BrandType]("isLike"))
    colStart.setCellValueFactory(new PropertyValueFactory[Brand, LocalDate]("start"))
    colEnd.setCellValueFactory(new PropertyValueFactory[Brand, LocalDate]("end"))
    colSize.setCellValueFactory(new PropertyValueFactory[Brand, Integer]("size"))

    colStart.setCellFactory(_ => new TableCell[Brand, LocalDate]() {
      override protected def updateItem(item: LocalDate, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setText(null)
        this.setGraphic(null)
        if (item != null && !empty)
          this.setText(String.valueOf(item.getYear))
      }
    })
    colEnd.setCellFactory(_ => new TableCell[Brand, LocalDate]() {
      override protected def updateItem(item: LocalDate, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setText(null)
        this.setGraphic(null)
        if (item != null && !empty)
          this.setText(String.valueOf(item.getYear))
      }
    })
    colWebsite.setCellFactory(_ => new TableCell[Brand, String]() {
      override protected def updateItem(item: String, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setGraphic(null)
        if (!empty && !Strings.isEmpty(item)) {
          val titleLabel = new Hyperlink
          titleLabel.setText(item)
          titleLabel.setOnAction(_ => Websites.open(item))
          this.setGraphic(titleLabel)
        }
      }
    })
    colCommand.setCellFactory(_ => new TableCell[Brand, Void]() {
      override protected def updateItem(item: Void, empty: Boolean) = {
        super.updateItem(item, empty)
        this.setGraphic(null)
        if (!empty) {
          val link = new Hyperlink("関連ゲーム")
          link.setOnAction(_ => {
            targetBrand = this.getTableRow.getItem
            onLoadProperty.set(true)
            onLoadProperty.set(false)
          })
          this.setGraphic(link)
        }
      }
    })


    val handler: ChangeListener[util.List[Brand]] = (_, _, newValue) => {
      if (newValue != null)
        tableBrand.setItems(FXCollections.observableArrayList(newValue))
    }


    //    class T extends ChangeListener[util.List[Brand]] {
    //      override def changed(observable: ObservableValue[_ <: util.List[Brand]], oldValue: util.List[Brand], newValue: util.List[Brand]): Unit = {
    //        if (newValue != null)
    //          tableBrand.setItems(FXCollections.observableArrayList(newValue))
    //      }
    //    }

    //for data
    brandService.valueProperty.addListener(handler)
    brandByNameService.valueProperty.addListener(handler)
    brandByCompService.valueProperty.addListener(handler)


    choiceBrandType.setItems(FXCollections.observableArrayList(BrandType.values: _*))
    choiceBrandType.setConverter(new StringConverter[BrandType]() {
      override def toString(brandType: BrandType) = brandType.name

      override def fromString(string: String) = BrandType.from(string)
    })

    onLoadProperty.addListener((_, _, newValue) => {
      if (newValue != null && newValue) {
        val text = targetBrand.name
        TabSelect.from.ifNotFind(() => {
          val conn = new CommonInfoTabController
          val tab = new Tab(text, conn.node)
          tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG.get))
          conn.load(targetBrand)
          tab
        }).select(text)
      }
    })
  }

  def load() = choiceBrandType.setValue(BrandType.CHECKING)

  @FXML
  private def choiceBrandType_OnAction(actionEvent: ActionEvent) = {
    brandType = choiceBrandType.getValue
    logger.debug("Value: {}", choiceBrandType.getValue)
    brandService.restart()
  }

  @FXML
  private def search_OnAction(actionEvent: ActionEvent) = {
    keyword = textBrandKey.getText
    logger.debug("Value: {}", keyword)

    val `type` = typeGroup.getSelectedToggle.getUserData.asInstanceOf[String].toInt
    if (`type` == 0)
      brandByNameService.restart()
    else
      brandByCompService.restart()
  }
}