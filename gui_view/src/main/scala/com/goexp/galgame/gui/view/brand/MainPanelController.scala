package com.goexp.galgame.gui.view.brand

import java.time.LocalDate

import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.task.TaskService
import com.goexp.galgame.gui.task.brand.search.{ByComp, ByName, ByType}
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{TabSelect, Tags, Websites}
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.javafx.cell.{NodeTableCell, TextTableCell}
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.util.StringConverter
import Tags.maker

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class MainPanelController extends DefaultController {

  @FXML var colStart: TableColumn[Brand, LocalDate] = _
  @FXML var colEnd: TableColumn[Brand, LocalDate] = _
  @FXML var colSize: TableColumn[Brand, Int] = _
  @FXML private var colTag: TableColumn[Brand, List[String]] = _

  @FXML private var textBrandKey: TextField = _
  @FXML private var tableBrand: TableView[Brand] = _
  @FXML private var colComp: TableColumn[Brand, String] = _
  @FXML private var colName: TableColumn[Brand, String] = _
  @FXML private var colWebsite: TableColumn[Brand, String] = _
  @FXML private var colState: TableColumn[Brand, BrandState] = _
  @FXML private var colCommand: TableColumn[Brand, Brand] = _
  @FXML private var choiceBrandType: ChoiceBox[BrandState] = _
  @FXML private var typeGroup: ToggleGroup = _
  @FXML private var btnSearch: Button = _

  private var brandType = BrandState.LIKE
  private var keyword: String = _

  final private val brandService = TaskService(new ByType(brandType))
  final private val brandByNameService = TaskService(new ByName(keyword))
  final private val brandByCompService = TaskService(new ByComp(keyword))

  override protected def initialize() = {
    def initTable() = {
      colComp.setCellValueFactory(p => new SimpleStringProperty(p.getValue.comp))
      colName.setCellValueFactory(p => new SimpleStringProperty(p.getValue.name))
      colTag.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.tag))
      colWebsite.setCellValueFactory(p => new SimpleStringProperty(p.getValue.website))
      colState.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.state))
      colStart.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.start))
      colEnd.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.end))
      colSize.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.size))
      colCommand.setCellValueFactory(p => new SimpleObjectProperty(p.getValue))

      colTag.setCellFactory(_ =>
        NodeTableCell { tag =>
          val hbox = new HBox
          hbox.setSpacing(5)
          hbox.getChildren.setAll(Tags.toNodes(tag.asJava))
          hbox
        }
      )

      colStart.setCellFactory { _ =>
        TextTableCell { startDate =>
          startDate.getYear.toString
        }
      }

      colEnd.setCellFactory(_ =>
        TextTableCell { endDate =>
          endDate.getYear.toString
        }
      )
      colWebsite.setCellFactory(_ =>
        NodeTableCell { website =>
          val titleLabel = new Hyperlink(website)
          titleLabel.setOnAction(_ => Websites.open(website))

          titleLabel
        }
      )

      colCommand.setCellFactory(_ =>
        NodeTableCell { brand =>

          val link = new Hyperlink("関連ゲーム")
          link.setOnAction(_ => {
            val text = brand.name
            TabSelect().whenNotFound {
              val conn = new CommonInfoTabController
              val tab = new Tab(text, conn.node)
              tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG))
              conn.load(brand)
              tab
            }.select(text)

          })

          link
        }
      )
    }

    initTable()


    val handler: ChangeListener[mutable.Buffer[Brand]] = (_, _, newValue) => {
      if (newValue != null)
        tableBrand.setItems(FXCollections.observableArrayList(newValue.asJava))
    }



    //for data
    brandService.valueProperty.addListener(handler)
    brandByNameService.valueProperty.addListener(handler)
    brandByCompService.valueProperty.addListener(handler)


    choiceBrandType.setItems(FXCollections.observableArrayList(BrandState.values: _*))
    choiceBrandType.setConverter(new StringConverter[BrandState]() {
      override def toString(brandType: BrandState) = brandType.name

      override def fromString(string: String) = BrandState.from(string)
    })
    choiceBrandType.setOnAction { _ =>
      brandType = choiceBrandType.getValue

      logger.debug(s"Value: ${choiceBrandType.getValue}")

      brandService.restart()

    }

    btnSearch.setOnAction { _ =>
      keyword = textBrandKey.getText

      logger.debug(s"Value: ${keyword}")

      val `type` = typeGroup.getSelectedToggle.getUserData.asInstanceOf[String].toInt
      if (`type` == 0)
        brandByNameService.restart()
      else
        brandByCompService.restart()

    }

  }

  def load() = choiceBrandType.setValue(BrandState.CHECKING)

}