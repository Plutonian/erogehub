package com.goexp.galgame.gui.view.common.jump

import com.goexp.galgame.common.website.{ErogameScapeURL, GGBasesURL, WikiURL, _2DFURL}
import com.goexp.galgame.gui.util.Websites
import com.goexp.ui.javafx.DefaultController
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.MenuItem

class SearchController extends DefaultController {
  final lazy val keyword = new SimpleStringProperty()

  @FXML private var linkGGBases: MenuItem = _
  @FXML private var linkWiki: MenuItem = _
  @FXML private var linkEgs: MenuItem = _
  @FXML private var link2DF: MenuItem = _

  override protected def eventBinding() = {
    linkGGBases.setOnAction(_ => Websites.open(GGBasesURL.fromTitle(keyword.get())))
    linkWiki.setOnAction(_ => Websites.open(WikiURL.fromTitle(keyword.get())))
    linkEgs.setOnAction(_ => Websites.open(ErogameScapeURL.fromTitle(keyword.get())))
    link2DF.setOnAction(_ => Websites.open(_2DFURL.fromTitle(keyword.get())))
  }

}