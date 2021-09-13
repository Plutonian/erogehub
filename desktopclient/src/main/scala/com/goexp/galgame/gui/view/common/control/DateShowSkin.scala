package com.goexp.galgame.gui.view.common.control

import com.goexp.common.util.date.DateUtil
import javafx.scene.control.SkinBase
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{StackPane, VBox}
import scalafx.scene.text.Font

import java.time.format.DateTimeFormatter

class DateShowSkin(control: DateShow) extends SkinBase[DateShow](control) {

  lazy val _date = control.dateProperty

  lazy val needFormat = _date.isNotNull.and(Bindings.createBooleanBinding { () => DateUtil.needFormat(_date.value) })

  lazy val notNeedFormat = _date.isNotNull and needFormat.not()

  lazy private val container = new StackPane() {
    maxWidth = 100
    effect = new DropShadow
  }

  lazy private val style1Container = new StackPane {
    alignment = Pos.TopRight
    prefHeight = 50.0

    children = Seq(
      //lbDays
      new Label() {
        font = Font(18)

        text <== when(needFormat) choose DateUtil.formatDate(_date.value) otherwise ""
      }
    )
  }

  lazy private val style2Container = new VBox {
    alignment = Pos.TopRight
    prefHeight = 50.0

    children = Seq(
      //lbYear
      new Label() {
        text <== when(notNeedFormat) choose _date.value.format(DateTimeFormatter.ofPattern("yyyy")) otherwise ""
        font = Font(24)
      },
      //lbMonth
      new Label() {
        text <== when(notNeedFormat) choose _date.value.format(DateTimeFormatter.ofPattern("MMM")) otherwise ""
        font = Font(14)
      },
      //lbDay
      new Label() {
        text <== when(notNeedFormat) choose _date.value.format(DateTimeFormatter.ofPattern("dd")) otherwise ""
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
    } else {

      if (DateUtil.needFormat(_date.value)) {
        container.children = Seq(style1Container)

      } else {
        container.children = Seq(style2Container)
      }
    }
    //
    //    if (date == null) {
    //      strPanel.setVisible(false)
    //      calPanel.setVisible(false)
    //      lbDays.setText("")
    //      return
    //    }
    //    if (DateUtil.needFormat(date)) {
    //      strPanel.setVisible(true)
    //      calPanel.setVisible(false)
    //      lbDays.setText(DateUtil.formatDate(date))
    //    }
    //    else {
    //      strPanel.setVisible(false)
    //      calPanel.setVisible(true)
    //
    //      lbYear.setText(date.format(DateTimeFormatter.ofPattern("yyyy")))
    //      lbMonth.setText(date.format(DateTimeFormatter.ofPattern("MMM")))
    //      lbDay.setText(date.format(DateTimeFormatter.ofPattern("dd")))
    //      calPanel.toFront()
    //    }

  }

}


