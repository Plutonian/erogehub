package com.goexp.galgame.gui.view.common.control

import com.goexp.common.util.date.DateUtil
import javafx.fxml.FXML
import javafx.scene.control.{Control, Label, Skin}
import javafx.scene.layout.Region
import scalafx.beans.property.ObjectProperty

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.beans.BeanProperty

class DateShow extends Control {

  @BeanProperty
  lazy val dateProperty = new ObjectProperty[LocalDate](this, "date", null)

  def date() = {
    dateProperty.get()
  }

  def date(tag: LocalDate) = {
    dateProperty.set(tag)
  }

  def this(tag: LocalDate) = {
    this()
    date(tag)
  }


  override def createDefaultSkin(): Skin[_] = new DateShowSkin(this)


  //  @FXML private var lbYear: Label = _
  //  @FXML private var lbMonth: Label = _
  //  @FXML private var lbDay: Label = _
  //  @FXML private var lbDays: Label = _
  //  @FXML private var calPanel: Region = _
  //  @FXML private var strPanel: Region = _
  //
  //  def load(date: LocalDate): Unit = {
  //    if (date == null) {
  //      strPanel.setVisible(true)
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
  //      lbYear.setText(date.format(DateTimeFormatter.ofPattern("yyyy")))
  //      lbMonth.setText(date.format(DateTimeFormatter.ofPattern("MMM")))
  //      lbDay.setText(date.format(DateTimeFormatter.ofPattern("dd")))
  //      calPanel.toFront()
  //    }
  //  }
}