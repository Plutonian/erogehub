package com.goexp.galgame.gui.view.common.jump

import com.goexp.galgame.gui.HGameApp
import com.goexp.ui.javafx.DefaultController
import javafx.beans.property.{SimpleIntegerProperty, SimpleStringProperty}
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import scalafx.Includes._

class JumpLinkController extends DefaultController {

  final lazy val name = new SimpleStringProperty()
  final lazy val id = new SimpleIntegerProperty()

  //  @FXML private var searchLinkController: SearchController = _
  //  @FXML private var linkGetchu: MenuItem = _
  @FXML private var linkGuide: MenuItem = _

  override protected def eventBinding() = {
    //    linkGetchu.setOnAction(_ => Websites.open(GetchuGameRemote.byId(id.value)))
    linkGuide.setOnAction(_ => HGameApp.loadGuide(name.value))
  }

}