package com.goexp.galgame.gui.view.common.control

import com.goexp.ui.javafx.control.cell.NodeTableCell
import javafx.scene.control.{Label, TableColumn}

class DataTableColumn[S] extends TableColumn[S, Number] {


  def this(text: String) = {
    this()
    setText(text)
  }

  setCellFactory { _ =>
    NodeTableCell { num =>
      new Label(num.toString) {
        if (num == 0) {
          setStyle("-fx-text-fill:#BBB")
        }
      }
    }
  }

}
