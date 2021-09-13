package com.goexp.galgame.gui.view.game.listview.simplelist.small

import com.goexp.galgame.common.model.game.CommonGame.Titles
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.{Brand, Game}
import com.goexp.galgame.gui.util.res.gameimg.GameImage
import com.goexp.galgame.gui.util.{Controller, SimpleFxmlLoader}
import com.goexp.galgame.gui.view.common.control.{FormattedDate, StarRatingView, TagPanel}
import com.goexp.galgame.gui.view.common.jump.JumpBrandController
import javafx.fxml.FXML
import scalafx.beans.property.StringProperty
import scalafx.scene.control.Hyperlink
import scalafx.scene.text.{Text, TextFlow}

import java.time.LocalDate
//import javafx.scene.control.Hyperlink
import javafx.scene.image.Image
import scalafx.Includes._
import scalafx.beans.property.{IntegerProperty, ObjectProperty}
import scalafx.geometry.Insets
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{BorderPane, VBox}

import java.util

class HeaderView extends BorderPane with Controller {

  import VO._

  object VO {
    val _image = new ObjectProperty[Image]()
    val _mainTitle = new StringProperty()
    val _subTitle = new StringProperty()

    val b = new ObjectProperty[Brand]()


    val _star = new IntegerProperty()
    val _tags = new ObjectProperty[util.List[String]]()
    val _publishDate = new ObjectProperty[LocalDate]()


  }


  stylesheets.add("/view/view.css")
  style = "-fx-background-color:white"

  padding = Insets(3)


  val brandJumpController = new SimpleFxmlLoader[JumpBrandController]("jumpbrand.fxml")
  //  loader.controller.load()

  center = new VBox {
    children = Seq(
      new ImageView() {
        pickOnBounds = true

        image <== _image
      },
      new BorderPane {
        right = new FormattedDate() {
          dateProperty <== _publishDate
        }
        center = new TextFlow {
          children = Seq(
            new Hyperlink() {
              text <== _mainTitle
              styleClass.add("mainTitle")
              wrapText = true

              onAction = _ => HGameApp.loadDetail(targetGame)
            },
            new Text {
              text <== _subTitle
              visible <== _subTitle.isNotEmpty
              managed <== _subTitle.isNotEmpty
            }
          )
        }
      },
      new VBox() {
        spacing = 3.0

        children ++= Seq(
          brandJumpController.node,
          new StarRatingView() {
            ratingProperty <== _star
          },
          new TagPanel() {
            tagsProperty <== _tags
          }
        )
      }
    )

  }


  //  textFlow.prefWidth <== imageImg.fitWidth.subtract(dateview.width)

  //  @FXML private var dateview: Pane = _

  //  @FXML private var changeStateController: StateChangeController = _

  //  @FXML private var lbLoc: Label = _

  @FXML var textFlow: TextFlow = _

  private var targetGame: Game = _


  def load(game: Game) = {
    _image.value =
      if (game.isOkImg) {
        GameImage(game).normal()
      } else {
        null
      }


    loadWithoutImage(game)

  }

  def setImage(image: Image) = _image.set(image)

  private def loadWithoutImage(game: Game) = {

    this.targetGame = game
    _star.set(game.star.get())


    brandJumpController.controller.load(game.brand)

    val Titles(mainTitle, subTitle) = game.getTitles
    _mainTitle.value = mainTitle

    _subTitle.value = subTitle

    _tags.value = game.tag

    _publishDate.value = game.publishDate


    //    changeStateController.load(game)


    //    lbLoc.setText {
    //      if (game.location.get() != GameLocation.REMOTE)
    //        (game.location.get().name)
    //      else null
    //    }

  }

  override def load(): Unit = {


  }
}