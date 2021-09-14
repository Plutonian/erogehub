package com.goexp.galgame.gui.view.game.detailview.inner

import com.goexp.common.util.Logger
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.common.website.{BangumiURL, WikiURL}
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.res.gameimg.PersonImage
import com.goexp.galgame.gui.util.{Controller, Websites}
import javafx.scene.image.Image
import scalafx.Includes._
import scalafx.beans.property.{BooleanProperty, ObjectProperty, StringProperty}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, MenuButton, MenuItem, SeparatorMenuItem}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{BorderPane, HBox, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}

class PersonCellPanel extends BorderPane with Logger with Controller {

  object VO {
    lazy val _cv = new StringProperty()
    lazy val _intro = new StringProperty()
    lazy val _name = new StringProperty()
    lazy val _isTrueCV = new BooleanProperty()
    lazy val _image = new ObjectProperty[Image]()
  }

  import VO._

  padding = Insets(10)

  left = new ImageView {
    image <== _image

    registestListener(image)
  }

  center = new VBox {
    margin = Insets(0, 0, 0, 10)

    children = Seq(
      //name
      new Text {
        text <== _name

        font = Font.font("System Bold", 18)

        registestListener(text)
      },
      new HBox {
        margin = (Insets(0, 0, 10, 0))
        visible <== _cv.isNotEmpty


        children = Seq(
          new Label {
            text = "CV"
            font = Font.font("System Bold", 18)
          },
          new MenuButton {
            text <== (_cv)
            textFill <==
              when(_isTrueCV).choose(Color.valueOf("red")) otherwise (Color.valueOf("black"))

            style = ("-fx-background-color: transparent")
            margin = (Insets(0, 0, 0, 5))

            items = Seq(
              new MenuItem {
                text = "関連ゲーム"
                onAction = _ => HGameApp.loadCVTab(_cv.get(), _isTrueCV.get())
              },
              new SeparatorMenuItem,
              new MenuItem {
                text = "Wiki"
                onAction = _ => Websites.open(WikiURL.fromTitle(_cv.get()))

              },
              new MenuItem {
                text = "Bangumi"
                onAction = _ => Websites.open(BangumiURL.fromTitle(_cv.get()))

              }

            )
          }
        )

        registestListener(visible)
      },
      new StackPane {
        style = ("-fx-background-color: #ccc;")
        padding = (Insets(5))

        visible <== _intro.isNotEmpty
        managed <== _intro.isNotEmpty

        children = Seq(
          new Text {
            text <== (_intro)
            fill = (Color.valueOf("#515151"))
            font = (Font.font(14))
            wrappingWidth = (600)
            alignment = (Pos.TopLeft)


            registestListener(text)
          }
        )


        registestListener(visible)
        registestListener(managed)
      }
    )
  }


  def load(g: Game, gchar: GameCharacter) = {

    logger.debug(s"${gchar}")

    val isTrueCV = Strings.isNotEmpty(gchar.trueCV)
    val cv = if (isTrueCV) gchar.trueCV else gchar.cv


    _name.set(gchar.name)
    _cv.set(cv)
    _isTrueCV.set(isTrueCV)
    _intro.set(gchar.intro)

    _image.set({

      if (Strings.isNotEmpty(gchar.img)) {
        new PersonImage(g).small(gchar.index)
      }
      else
        null

    })


  }

  override def load(): Unit = {

  }

}
