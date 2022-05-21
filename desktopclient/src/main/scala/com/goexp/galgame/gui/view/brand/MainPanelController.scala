package com.goexp.galgame.gui.view.brand

import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.task.brand.search.{ByComp, ByName, ByType}
import com.goexp.galgame.gui.util.Tags.maker
import com.goexp.galgame.gui.util.{Tags, Websites}
import com.goexp.ui.javafx.control.cell.NodeTableCell
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout.HBox
import scalafx.Includes._

import java.util
import scala.jdk.CollectionConverters._

class MainPanelController extends DefaultController {

  @FXML private var colTag: TableColumn[Brand, List[String]] = _

  @FXML private var textBrandKey: TextField = _

  @FXML private var tableBrand: TableView[Brand] = _
  @FXML private var colComp: TableColumn[Brand, String] = _
  @FXML private var colName: TableColumn[Brand, String] = _
  @FXML private var colWebsite: TableColumn[Brand, String] = _
  @FXML private var colState: TableColumn[Brand, BrandState] = _
  @FXML private var choiceBrandType: ChoiceBox[BrandState] = _
  @FXML private var typeGroup: ToggleGroup = _
  @FXML private var btnSearch: Button = _
  @FXML private var btn_reload: Button = _

  @FXML private var lbCount: Label = _
  @FXML private var progessloading: ProgressBar = _


  private object VO {
    lazy val keyword = new SimpleStringProperty
    lazy val brandType = new SimpleObjectProperty[BrandState]
  }

  import VO._

  final private val brandListByStateService = TaskService(new ByType(brandType.get()))
  final private val brandSearchByNameService = TaskService(new ByName(keyword.get))
  final private val brandSearchByCompService = TaskService(new ByComp(keyword.get))


  override protected def eventBinding(): Unit = {
    val handler: ChangeListener[util.List[Brand]] = (_, _, brands) => {
      if (brands != null) {
        val list = FXCollections.observableArrayList(brands)
        tableBrand.setItems(list)

        lbCount.setText(s"${list.size} 件")

      }
    }

    //for data
    brandListByStateService.valueProperty.addListener(handler)
    brandSearchByNameService.valueProperty.addListener(handler)
    brandSearchByCompService.valueProperty.addListener(handler)


    choiceBrandType.getSelectionModel.selectedItem.onChange { (_, oldValue, newValue) =>
      if (newValue != null) {

        logger.debug(s"Old:${oldValue} New: ${newValue}")

        brandListByStateService.restart()
      }
    }

    btnSearch.setOnAction { _ =>

      logger.debug(s"Value: ${keyword.get}")

      val t = typeGroup.getSelectedToggle.getUserData.asInstanceOf[String].toInt
      if (t == 0)
        brandSearchByNameService.restart()
      else
        brandSearchByCompService.restart()

    }

    btn_reload.setOnAction { _ =>
      brandListByStateService.restart()
    }
  }

  override protected def dataBinding(): Unit = {

    brandType <== choiceBrandType.getSelectionModel.selectedItem

    progessloading.visible <== brandListByStateService.running
    keyword <== textBrandKey.text
    btnSearch.disable <== textBrandKey.text.isEmpty
  }

  override protected def initComponent(): Unit = {

    colComp.setCellValueFactory(p => new SimpleStringProperty(p.getValue.comp))
    colName.setCellValueFactory(p => new SimpleStringProperty(p.getValue.name))
    colTag.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.tag))
    colWebsite.setCellValueFactory(p => new SimpleStringProperty(p.getValue.website))
    colState.setCellValueFactory(p => p.getValue.state)

    colName.setCellFactory(_ => {

      NodeTableCell { (brand, _) =>

        if (brand != null) {
          val link = new Hyperlink(brand.name)
          link.setOnAction(_ => {
            HGameApp.viewBrand(brand)
          })
          link
        } else {
          null
        }

      }
    })


    colTag.setCellFactory(_ => {
      val hbox = new HBox
      hbox.setSpacing(5)

      NodeTableCell { tag =>
        hbox.getChildren.setAll(Tags.toNodes(tag.asJava))
        hbox
      }
    })

    colWebsite.setCellFactory(_ => {
      var url: String = null
      val titleLabel = new Hyperlink()
      titleLabel.setOnAction(_ => Websites.open(url))

      NodeTableCell { website =>
        url = website
        titleLabel.setText(website)

        titleLabel
      }
    })

    choiceBrandType.setItems(FXCollections.observableArrayList(BrandState.values.reverse: _*))
  }

}