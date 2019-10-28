package com.goexp.galgame.gui.view.game.detailview.part

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.gui.view.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.Region

class DateShowController extends DefaultController {
  @FXML private var lbYear: Label = _
  @FXML private var lbMonth: Label = _
  @FXML private var lbDay: Label = _
  @FXML private var lbDays: Label = _
  @FXML private var calPanel: Region = _
  @FXML private var strPanel: Region = _

  def load(date: LocalDate): Unit = {
    if (date == null) {
      strPanel.setVisible(true)
      calPanel.setVisible(false)
      lbDays.setText("")
      return
    }
    if (DateUtil.needFormat(date)) {
      strPanel.setVisible(true)
      calPanel.setVisible(false)
      lbDays.setText(DateUtil.formatDate(date))
    }
    else {
      strPanel.setVisible(false)
      calPanel.setVisible(true)
      lbYear.setText(date.format(DateTimeFormatter.ofPattern("yyyy")))
      lbMonth.setText(date.format(DateTimeFormatter.ofPattern("MMM")))
      lbDay.setText(date.format(DateTimeFormatter.ofPattern("dd")))
      calPanel.toFront()
    }
  }

  override protected def initialize() = {
  }
}