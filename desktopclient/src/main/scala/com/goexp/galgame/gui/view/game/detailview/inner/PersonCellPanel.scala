package com.goexp.galgame.gui.view.game.detailview.inner

import com.goexp.common.util.Logger
import com.goexp.common.util.string.Strings
import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.common.website.{BangumiURL, WikiURL}
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.util.Websites
import com.goexp.galgame.gui.util.res.gameimg.PersonImage
import javafx.beans.binding.Bindings
import javafx.beans.property.{SimpleBooleanProperty, SimpleObjectProperty, SimpleStringProperty}
import javafx.geometry.{Insets, Pos}
import javafx.scene.control.{Label, MenuButton, MenuItem, SeparatorMenuItem}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{BorderPane, HBox, StackPane, VBox}
import javafx.scene.paint.Color
import javafx.scene.text.{Font, Text}

class PersonCellPanel extends BorderPane with Logger {

  private val cv = new SimpleStringProperty()
  private val intro = new SimpleStringProperty()
  private val name = new SimpleStringProperty()
  private val isTrueCV = new SimpleBooleanProperty()
  private val image = new SimpleObjectProperty[Image]()


  //  private val isTrueCV = Strings.isNotEmpty(gameChar.trueCV)
  //  private val cv = if (isTrueCV) s"*${gameChar.trueCV}*" else gameChar.cv


  class GameCharacterInfo extends VBox {


    class CVPanel extends HBox {

      /**
       * 関連ゲーム
       * ----------
       * +++++++++
       */
      class CVMenu extends MenuButton {
        textProperty().bind(cv)
        textFillProperty().bind(
          Bindings
            .when(isTrueCV)
            .`then`(Color.valueOf("red"))
            .otherwise(Color.valueOf("black"))
        )
        setStyle("-fx-background-color: transparent")
        HBox.setMargin(this, new Insets(0, 0, 0, 5))

        getItems.setAll(
          new MenuItem("関連ゲーム") {
            setOnAction { _ =>
              HGameApp.loadCVTab(cv.get(), isTrueCV.get())
            }

          },
          new SeparatorMenuItem,
          new MenuItem("Wiki") {
            setOnAction(_ => Websites.open(WikiURL.fromTitle(cv.get())))

          },
          new MenuItem("Bangumi") {
            setOnAction(_ => Websites.open(BangumiURL.fromTitle(cv.get())))

          },

          //TODO query
          //      ,
          //      new MenuItem("関連ゲーム")
        )

      }


      VBox.setMargin(this, new Insets(0, 0, 10, 0))
      getChildren.setAll(
        new Label("CV") {
          setFont(Font.font("System Bold", 18))
        },
        new CVMenu
      )
    }

    class GameCharacterDesc extends StackPane {
      setStyle("-fx-background-color: #ccc;")
      setPadding(new Insets(5))

      getChildren.setAll(new Text {
        textProperty().bind(intro)
        setFill(Color.valueOf("#515151"))
        setFont(Font.font(14))
        setWrappingWidth(600)

        setAlignment(Pos.TOP_LEFT)

      })
    }

    //    setPadding(new Insets(5))
    BorderPane.setMargin(this, new Insets(0, 0, 0, 10))

    //Name
    getChildren.setAll(
      new Text {
        textProperty().bind(name)
        setFont(Font.font("System Bold", 18))
      }
      , new CVPanel {
        visibleProperty().bind(Bindings.isNotEmpty(cv))
      }
      , new GameCharacterDesc {
        visibleProperty().bind(Bindings.isNotEmpty(intro))
        managedProperty().bind(Bindings.isNotEmpty(intro))
      }
    )

  }


  setPadding(new Insets(10))

  setLeft(new ImageView {
    //    setPadding(new Insets(5))
    imageProperty().bind(image)
  })

  setCenter(new GameCharacterInfo)


  def load(g: Game, gchar: GameCharacter) = {

    logger.debug(s"${gchar}")

    val isTrueCV = Strings.isNotEmpty(gchar.trueCV)
    val cv = if (isTrueCV) gchar.trueCV else gchar.cv


    name.set(gchar.name)
    this.cv.set(cv)
    this.isTrueCV.set(isTrueCV)
    intro.set(gchar.intro)

    image.set({

      if (Strings.isNotEmpty(gchar.img)) {
        new PersonImage(g).small(gchar.index)
      }
      else
        null

    })


  }

  //  @FXML private var txtIntro: Text = _
  //
  //  def init() = {
  //
  //    if (Strings.isEmpty(gameChar.intro))
  //      txtIntro.setVisible(false)
  //    else
  //      txtIntro.setText(gameChar.intro)
  //
  //  }

}
