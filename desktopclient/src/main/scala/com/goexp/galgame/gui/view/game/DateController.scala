package com.goexp.galgame.gui.view.game

import com.goexp.galgame.gui.task.game.search.ByDateRange
import com.goexp.galgame.gui.util.TabManager
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.ui.javafx.DefaultController
import com.goexp.ui.javafx.control.cell.TextListCell
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.{ListView, Tab}
import javafx.scene.image.ImageView

import java.time.LocalDate
import scala.jdk.CollectionConverters._

class DateController extends DefaultController {
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
        val from = LocalDate.now.withMonth(month).withDayOfMonth(1)
        val to = from.plusMonths(1).minusDays(1)
        val title = s"${month}月"

        logger.debug(s"Range:${from}  ${to}")

        val conn = CommonTabController(new ByDateRange(from, to))

        TabManager().open(title, {
          new Tab(title, conn.node) {
            setGraphic(new ImageView(LocalRes.DATE_16_PNG))
          }
        }) {
          conn.load()
        }

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
        val from = LocalDate.of(year, 1, 1)
        val to = LocalDate.of(year, 12, 31)
        val title = s"${year}年"

        logger.debug(s"Range:${from}  ${to}")

        val conn = CommonTabController(new ByDateRange(from, to))

        TabManager().open(title, {
          new Tab(title, conn.node) {

            setGraphic(new ImageView(LocalRes.DATE_16_PNG))
          }
        }) {
          conn.load()
        }

      }

      val years = (2000 to LocalDate.now.getYear + 1).reverse.asJava

      flowYear.setItems(FXCollections.observableArrayList(years))

    }

    initYear()

  }
}