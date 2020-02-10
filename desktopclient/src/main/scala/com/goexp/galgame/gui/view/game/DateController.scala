package com.goexp.galgame.gui.view.game

import java.time.LocalDate


import com.goexp.ui.javafx.DefaultController
import com.goexp.ui.javafx.control.cell.TextListCell
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ListView

import scala.jdk.CollectionConverters._

class DateController extends DefaultController {
  final val onLoadProperty = new SimpleBooleanProperty(false)
  final val onYearLoadProperty = new SimpleBooleanProperty(false)

  var title: String = _
  var from: LocalDate = _
  var to: LocalDate = _
  @FXML private var flowMonth: ListView[Int] = _
  @FXML private var flowYear: ListView[Int] = _

  override protected def initialize() = {


    def initMonth() = {

      //cell
      flowMonth.setCellFactory { _ =>
        TextListCell[Int] { month =>
          if (month == LocalDate.now.getMonthValue) "今月" else s"${month}月"
        }
      }

      //selcet event
      flowMonth.getSelectionModel().selectedItemProperty().addListener { (_, _, month) =>
        from = LocalDate.now.withMonth(month).withDayOfMonth(1)
        to = from.plusMonths(1).minusDays(1)
        title = s"${month}月"
        onLoadProperty.set(true)

        logger.debug(s"Range:${from}  ${to}")

        onLoadProperty.set(false)
      }

      val months = (1 to 12).asJava

      flowMonth.setItems(FXCollections.observableArrayList(months))

    }

    initMonth()


    def initYear() = {

      //cell
      flowYear.setCellFactory { _ =>
        TextListCell[Int] { year =>
          if (year == LocalDate.now.getYear) "今年" else s"${year}年"
        }
      }

      //selcet event
      flowYear.getSelectionModel().selectedItemProperty().addListener { (_, _, year) =>
        from = LocalDate.of(year, 1, 1)
        to = LocalDate.of(year, 12, 31)
        title = s"${year}年"
        onYearLoadProperty.set(true)

        logger.debug(s"Range:${from}  ${to}")

        onYearLoadProperty.set(false)
      }

      val years = (2000 to LocalDate.now.getYear + 1).asJava

      flowYear.setItems(FXCollections.observableArrayList(years))

    }

    initYear()

  }

  def load() = onLoadProperty.set(false)
}