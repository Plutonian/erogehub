package com.goexp.galgame.gui.view.brand

import java.time.LocalDate
import java.util

import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.common.model.game.brand.BrandState
import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.task.brand.search.{ByComp, ByName, ByType}
import com.goexp.galgame.gui.util.Tags.maker
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{TabSelect, Tags, Websites}
import com.goexp.ui.javafx.control.cell.{NodeTableCell, TextTableCell}
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

import scala.jdk.CollectionConverters._

class MainPanelController extends DefaultController {

  @FXML var colStart: TableColumn[Brand, LocalDate] = _
  @FXML var colEnd: TableColumn[Brand, LocalDate] = _

  @FXML var colCount: TableColumn[Brand, Int] = _
  @FXML var colRealCount: TableColumn[Brand, Int] = _

  @FXML var colPlayed: TableColumn[Brand, Int] = _
  @FXML var colPlaying: TableColumn[Brand, Int] = _
  @FXML var colHope: TableColumn[Brand, Int] = _
  @FXML var colViewLater: TableColumn[Brand, Int] = _
  @FXML var colUncheck: TableColumn[Brand, Int] = _

  @FXML var colZero: TableColumn[Brand, Int] = _
  @FXML var colOne: TableColumn[Brand, Int] = _
  @FXML var colTwo: TableColumn[Brand, Int] = _
  @FXML var colThree: TableColumn[Brand, Int] = _
  @FXML var colFour: TableColumn[Brand, Int] = _
  @FXML var colFive: TableColumn[Brand, Int] = _

  @FXML var colLocal: TableColumn[Brand, Int] = _
  @FXML var colNetdisk: TableColumn[Brand, Int] = _
  @FXML var colRemote: TableColumn[Brand, Int] = _

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
  @FXML var progessloading: ProgressBar = _


  private var brandType = BrandState.LIKE
  private lazy val keyword = new SimpleStringProperty

  final private val brandListByStateService = TaskService(new ByType(brandType))
  final private val brandSearchByNameService = TaskService(new ByName(keyword.get))
  final private val brandSearchByCompService = TaskService(new ByComp(keyword.get))

  override protected def initialize() = {
    def initTable() = {

      colPlayed.setText(GameState.PLAYED.name)
      colPlaying.setText(GameState.PLAYING.name)
      colHope.setText(GameState.HOPE.name)
      colViewLater.setText(GameState.READYTOVIEW.name)
      colUncheck.setText(GameState.UNCHECKED.name)

      colComp.setCellValueFactory(p => new SimpleStringProperty(p.getValue.comp))
      colName.setCellValueFactory(p => new SimpleStringProperty(p.getValue.name))
      colTag.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.tag))
      colWebsite.setCellValueFactory(p => new SimpleStringProperty(p.getValue.website))
      colState.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.state))
      colStart.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.start))
      colEnd.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.end))

      colCount.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.count))
      colRealCount.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.realCount))
      colPlayed.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.played))
      colPlaying.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.playing))
      colHope.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.hope))
      colViewLater.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.viewLater))
      colUncheck.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.state.uncheck))


      colZero.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.zero))
      colOne.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.one))
      colTwo.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.two))
      colThree.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.three))
      colFour.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.four))
      colFive.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.star.five))


      colLocal.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.location.local))
      colNetdisk.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.location.netdisk))
      colRemote.setCellValueFactory(p => new SimpleObjectProperty(p.getValue.statistics.location.remote))

      colName.setCellFactory(_ => {

        NodeTableCell { (brand, _) =>

          if (brand != null) {
            val link = new Hyperlink(brand.name)
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

    }

    initTable()


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


    choiceBrandType.setItems(FXCollections.observableArrayList(BrandState.values.reverse: _*))

    choiceBrandType.getSelectionModel.selectedItemProperty().addListener { (_, _, t) =>
      if (t != null) {

        brandType = t

        logger.debug(s"Value: ${t}")

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

    progessloading.visibleProperty().bind(brandListByStateService.runningProperty())

    textBrandKey.textProperty().bindBidirectional(keyword)
    btnSearch.disableProperty().bind(textBrandKey.textProperty().isEmpty)

  }

  def load() = {}

}