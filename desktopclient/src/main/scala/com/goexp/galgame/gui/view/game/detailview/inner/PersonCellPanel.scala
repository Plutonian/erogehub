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

  private val cv = new StringProperty()
  private val intro = new StringProperty()
  private val name = new StringProperty()
  private val isTrueCV = new BooleanProperty()
  private val img = new ObjectProperty[Image]()


  padding = Insets(10)

  left = new ImageView {
    image <== img

    registestListener(image)
  }

  center = new VBox {
    margin = Insets(0, 0, 0, 10)

    children = Seq(
      //name
      new Text {
        text <== name

        font = Font.font("System Bold", 18)

        registestListener(text)
      },
      new HBox {
        margin = (Insets(0, 0, 10, 0))
        visible <== cv.isNotEmpty


        children = Seq(
          new Label {
            text = "CV"
            font = Font.font("System Bold", 18)
          },
          new MenuButton {
            text <== (cv)
            textFill <==
              when(isTrueCV).choose(Color.valueOf("red")) otherwise (Color.valueOf("black"))

            style = ("-fx-background-color: transparent")
            margin = (Insets(0, 0, 0, 5))

            items = Seq(
              new MenuItem {
                text = "関連ゲーム"
                onAction = _ => HGameApp.loadCVTab(cv.get(), isTrueCV.get())
              },
              new SeparatorMenuItem,
              new MenuItem {
                text = "Wiki"
                onAction = _ => Websites.open(WikiURL.fromTitle(cv.get()))

              },
              new MenuItem {
                text = "Bangumi"
                onAction = _ => Websites.open(BangumiURL.fromTitle(cv.get()))

              }

            )
          }
        )

        registestListener(visible)
      },
      new StackPane {
        style = ("-fx-background-color: #ccc;")
        padding = (Insets(5))

        visible <== intro.isNotEmpty
        managed <== intro.isNotEmpty

        children = Seq(
          new Text {
            text <== (intro)
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


    name.set(gchar.name)
    this.cv.set(cv)
    this.isTrueCV.set(isTrueCV)
    intro.set(gchar.intro)

    img.set({

      if (Strings.isNotEmpty(gchar.img)) {
        new PersonImage(g).small(gchar.index)
      }
      else
        null

    })


  }

  override def load(): Unit = {

  }

  //  override def dispose(): Unit = {
  //    super.dispose()
  //  }
}
