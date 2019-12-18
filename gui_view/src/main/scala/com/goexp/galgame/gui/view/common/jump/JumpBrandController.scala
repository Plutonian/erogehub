package com.goexp.galgame.gui.view.common.jump

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.website.getchu.GameList
import com.goexp.galgame.gui.model.Brand
import com.goexp.galgame.gui.util.Websites
import com.goexp.galgame.gui.view.{DefaultController, MainController}
import javafx.fxml.FXML
import javafx.scene.control.{MenuButton, MenuItem}

class JumpBrandController extends DefaultController {
  private var brand = new Brand
  @FXML private var searchLinkController: SearchController = _
  @FXML private var brandLabel: MenuButton = _
  @FXML private var jumpBrand: MenuItem = _
  @FXML private var linkWebsite: MenuItem = _
  @FXML private var linkGetchu: MenuItem = _

  override protected def initialize() = {
    linkGetchu.setOnAction(_ => Websites.open(GameList.byBrand(brand.id)))
    jumpBrand.setOnAction(_ => MainController().viewBrand(brand))
    linkWebsite.setOnAction(_ => Websites.open(brand.website))
  }

  def load(brand: Brand) = {
    require(brand != null)

    this.brand = brand
    brandLabel.setText(brand.name)
    linkWebsite.setVisible(Strings.isNotEmpty(brand.website))
    searchLinkController.load(brand.name)
  }
}