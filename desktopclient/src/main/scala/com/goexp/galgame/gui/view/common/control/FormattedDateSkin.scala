package com.goexp.galgame.gui.view.common.control

import com.goexp.common.util.date.DateUtil
import javafx.scene.control.SkinBase
import scalafx.Includes._
import scalafx.beans.property.{BooleanProperty, StringProperty}
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{StackPane, VBox}
import scalafx.scene.text.Font

import java.time.format.DateTimeFormatter

class FormattedDateSkin(control: FormattedDate) extends SkinBase[FormattedDate](control) {

  import VO._

  object VO {
    val _year = new StringProperty()
    val _month = new StringProperty()
    val _day = new StringProperty()
    val _fdate = new StringProperty()


    val needFormat = new BooleanProperty()
  }

  val _date = control.dateProperty


  private val container = new StackPane() {
    maxWidth = 100
    effect = new DropShadow
  }

  private val style1Container = new StackPane {
    alignment = Pos.TopRight
    prefHeight = 50.0

    visible <== needFormat

    children = Seq(
      //lbDays
      new Label() {
        font = Font(18)

        text <== _fdate
      }
    )
  }

  private val style2Container = new VBox {
    alignment = Pos.TopRight
    prefHeight = 50.0

    visible <== needFormat.not()

    children = Seq(
      //lbYear
      new Label() {
        text <== _year
        font = Font(24)
      },
      //lbMonth
      new Label() {
        text <== _month
        font = Font(14)
      },
      //lbDay
      new Label() {
        text <== _day
        font = Font(10)
      }
    )
  }

  init()

  private def init() = {
    control.setMouseTransparent(true)

    //    container
    getChildren.setAll(container)
    reCreate()

    registerChangeListener(control.dateProperty, {
      _ => reCreate()
    })
  }


  def reCreate(): Unit = {

    if (_date.value == null) {
      container.children.clear()
      needFormat.value = false
    } else {

      needFormat.value = DateUtil.needFormat(_date.value)

      if (DateUtil.needFormat(_date.value)) {
        _fdate.value = DateUtil.formatDate(_date.value)
        container.children = Seq(style1Container)

      } else {
        _year.value = _date.value.format(DateTimeFormatter.ofPattern("yyyy"))
        _month.value = _date.value.format(DateTimeFormatter.ofPattern("MMM"))
        _day.value = _date.value.format(DateTimeFormatter.ofPattern("dd"))

        container.children = Seq(style2Container)
      }
    }
  }

}


