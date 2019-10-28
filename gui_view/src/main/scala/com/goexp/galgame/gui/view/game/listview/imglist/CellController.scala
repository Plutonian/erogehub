package com.goexp.galgame.gui.view.game.listview.imglist

import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Tags
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.view.DefaultController
import com.goexp.galgame.gui.view.game.HomeController
import com.goexp.galgame.gui.view.game.part.StateChangeController
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{FlowPane, HBox}
import javafx.scene.text.Text

class CellController extends DefaultController {
  private var game: Game = _
  @FXML private var changeStateController: StateChangeController = _
  @FXML private var imageImg: ImageView = _
  @FXML private var boxStar: HBox = _
  @FXML private var txtName: Text = _
  @FXML private var txtSubName: Text = _
  @FXML private var lbDate: Label = _
  @FXML private var lbBrand: Label = _
  @FXML private var flowTag: FlowPane = _

  override protected def initialize() = {
  }

  def load(game: Game) = {
    this.game = game
    val titles = game.getTitles
    txtName.setText(titles.mainTitle)
    txtSubName.setText(titles.subTitle)
    lbBrand.setText(game.brand.name)
    lbDate.setText(DateUtil.formatDate(game.publishDate))
    changeStateController.load(game)
    if (game.tag.size > 0) flowTag.getChildren.setAll(Tags.toNodes(game.tag))

    if (game.isOkImg) {
      imageImg.setImage(new GameImage(game).normal())
      if (game.state.get eq GameState.BLOCK)
        imageImg.setEffect(new ColorAdjust(0, -1, 0, 0))
      else
        imageImg.setEffect(null)
    }
    else
      imageImg.setImage(null)

    val image = LocalRes.HEART_32_PNG

    boxStar.getChildren.clear()
    val stars = Range(0, game.star).to(LazyList).map { _ => new ImageView(image) }.toArray
    boxStar.getChildren.addAll(stars: _*)

  }

  def onClick(mouseEvent: MouseEvent) =
    HomeController.$this.loadDetail(game)
}