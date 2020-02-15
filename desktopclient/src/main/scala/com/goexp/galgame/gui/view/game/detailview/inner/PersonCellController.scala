package com.goexp.galgame.gui.view.game.detailview.inner

import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.res.gameimg.PersonImage
import com.goexp.galgame.gui.view.MainController
import com.goexp.galgame.gui.view.game.detailview.part.CVSearchController
import com.goexp.ui.javafx.DefaultController
import javafx.fxml.FXML
import javafx.scene.control.{MenuButton, MenuItem}
import javafx.scene.image.ImageView
import javafx.scene.layout.Region
import javafx.scene.text.Text

class PersonCellController extends DefaultController {
  @FXML private var cvsearchController: CVSearchController = _
  @FXML private var imageImg: ImageView = _
  @FXML private var txtName: Text = _
  @FXML private var lbCV: MenuButton = _
  @FXML private var truecv: MenuItem = _
  @FXML private var txtIntro: Text = _
  @FXML private var cvPart: Region = _

  def init(game: Game, gameChar: GameCharacter) = {

    logger.debug(s"${gameChar}")

    txtName.setText(gameChar.name)
    val isTrueCV = Strings.isNotEmpty(gameChar.trueCV)
    val cv = if (isTrueCV) s"*${gameChar.trueCV}*" else gameChar.cv

    if (Strings.isNotEmpty(cv)) {
      cvPart.setVisible(true)
      cvsearchController.load(cv)
      lbCV.setText(cv)
      truecv.setOnAction(_ => {
        val sCV = if (isTrueCV) gameChar.trueCV else gameChar.cv
        MainController().loadCVTab(sCV, isTrueCV)
      })
    }
    else
      cvPart.setVisible(false)

    if (Strings.isEmpty(gameChar.intro))
      txtIntro.setVisible(false)
    else
      txtIntro.setText(gameChar.intro)

    if (Strings.isNotEmpty(gameChar.img)) {
      val image = new PersonImage(game).small(gameChar.index)
      imageImg.setImage(image)
    }
    else
      imageImg.setImage(null)
  }

  override protected def initialize() = {
  }
}