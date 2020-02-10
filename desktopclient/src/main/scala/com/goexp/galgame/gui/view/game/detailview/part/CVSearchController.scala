package com.goexp.galgame.gui.view.game.detailview.part

import com.goexp.galgame.common.website.{BangumiURL, WikiURL}
import com.goexp.galgame.gui.util.Websites
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.MenuItem

class CVSearchController extends DefaultController {
  private var keyword = ""
  @FXML private var linkWiki: MenuItem = _
  @FXML private var linkBangumi: MenuItem = _

  override protected def initialize() = {
    linkWiki.setOnAction(_ => Websites.open(WikiURL.fromTitle(keyword)))
    linkBangumi.setOnAction(_ => Websites.open(BangumiURL.fromTitle(keyword)))
  }

  def load(keyword: String): Unit = this.keyword = keyword
}