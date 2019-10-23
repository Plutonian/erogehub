package com.goexp.galgame.gui.view.common.jump

import com.goexp.galgame.common.website.{GGBasesURL, WikiURL, _2DFURL}
import com.goexp.galgame.gui.util.Websites
import com.goexp.galgame.gui.view.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.MenuItem

class SearchController extends DefaultController {
  private var keyword: String = _

  @FXML private var linkGGBases: MenuItem = _
  @FXML private var linkWiki: MenuItem = _
  @FXML private var link2DF: MenuItem = _

  override protected def initialize() = {
    linkGGBases.setOnAction(_ => Websites.open(GGBasesURL.fromTitle(keyword)))
    linkWiki.setOnAction(_ => Websites.open(WikiURL.fromTitle(keyword)))
    link2DF.setOnAction(_ => Websites.open(_2DFURL.fromTitle(keyword)))
  }

  def load(keyword: String) = this.keyword = keyword
}