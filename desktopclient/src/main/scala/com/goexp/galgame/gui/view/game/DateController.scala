package com.goexp.galgame.gui.view.game

import java.time.LocalDate

import com.goexp.galgame.gui.view.DefaultController
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.{ToggleButton, ToggleGroup}
import javafx.scene.layout.{FlowPane, TilePane}

import scala.jdk.CollectionConverters._

class DateController extends DefaultController {
  final val onLoadProperty = new SimpleBooleanProperty(false)
  final val onYearLoadProperty = new SimpleBooleanProperty(false)

  var title: String = _
  var from: LocalDate = _
  var to: LocalDate = _
  @FXML private var dateCon: TilePane = _
  @FXML private var flowYear: FlowPane = _
  final private val yearSelect = new ToggleGroup
  final private val monthSelect = new ToggleGroup

  override protected def initialize() = {

    val monthNodes = Range.inclusive(1, 12).to(LazyList)
      .map { month =>
        val tog = new ToggleButton
        tog.setUserData(month)
        val mouthStr = if (month == LocalDate.now.getMonthValue) "今月" else s"${month}月"

        tog.setText(mouthStr)
        tog.setToggleGroup(monthSelect)
        tog
      }.asJava

    dateCon.getChildren.setAll(monthNodes)

    monthSelect.selectedToggleProperty.addListener((_, _, newV) => {
      if (newV != null) {
        val month = newV.getUserData.asInstanceOf[Int]
        from = LocalDate.now.withMonth(month).withDayOfMonth(1)
        to = from.plusMonths(1).minusDays(1)
        title = s"${month}月"
        onLoadProperty.set(true)

        logger.debug(s"Range:${from}  ${to}")

        onLoadProperty.set(false)
      }

    })

    val yearNodes = Range.inclusive(2000, LocalDate.now.getYear + 1).to(LazyList)
      .map(year => {
        val tog = new ToggleButton
        tog.setUserData(year)
        val yearStr = if (year == LocalDate.now.getYear) "今年" else s"${year}年"

        tog.setText(yearStr)
        tog.setToggleGroup(yearSelect)
        tog
      })
      .asJava

    flowYear.getChildren.setAll(yearNodes)

    yearSelect.selectedToggleProperty.addListener { (_, _, newV) =>
      if (newV != null) {
        val year = newV.getUserData.asInstanceOf[Int]
        from = LocalDate.of(year, 1, 1)
        to = LocalDate.of(year, 12, 31)
        title = s"${year}年"
        onYearLoadProperty.set(true)

        logger.debug(s"Range:${from}  ${to}")

        onYearLoadProperty.set(false)
      }

    }
  }

  def load() = onLoadProperty.set(false)
}