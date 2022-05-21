package com.goexp.galgame.gui.view.game

import com.goexp.galgame.common.model.CV
import com.goexp.galgame.common.model.game.GameState
import com.goexp.galgame.gui.HGameApp
import com.goexp.galgame.gui.task.CVListTask
import com.goexp.galgame.gui.util.Tags.maker
import com.goexp.galgame.gui.util.res.LocalRes
import com.goexp.galgame.gui.util.{Controller, Tags}
import com.goexp.galgame.gui.view.VelocityTemplateConfig
import com.goexp.ui.javafx.control.cell.{NodeTableCell, TextTableCell}
import com.goexp.ui.javafx.{DefaultController, TaskService}
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import org.apache.velocity.VelocityContext

import java.time.LocalDate
import scala.jdk.CollectionConverters._


class CVInfoController extends DefaultController with Controller {
  @FXML private var tableCV: TableView[CV] = _
  @FXML private var colName: TableColumn[CV, String] = _
  @FXML private var colStar: TableColumn[CV, Int] = _
  @FXML private var colTag: TableColumn[CV, List[String]] = _

  @FXML private var cvWebView: WebView = _


  final private val loadCVService = TaskService(new CVListTask())


  override protected def initComponent(): Unit = {

    colName.setCellValueFactory(param => new SimpleStringProperty(param.getValue.name))
    colStar.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.star))

    colTag.setCellValueFactory(param => new SimpleObjectProperty(param.getValue.tag))

    colStar.setCellFactory { _ =>
      val image = LocalRes.HEART_16_PNG
      val box = new HBox()

      NodeTableCell { star =>
        if (star > 0) {
          val stars = Range(0, star).to(LazyList).map { _ => new ImageView(image) }.asJava
          box.getChildren.setAll(stars)
          box
        }
        else new Label(star.toString)
      }
    }

    colName.setCellFactory { _ =>
      val link = new Hyperlink()

      NodeTableCell { name =>
        link.setText(name)
        link.setOnAction(_ => HGameApp.loadCVTab(name, true))
        link
      }
    }

    colTag.setCellFactory(_ => {
      val hbox = new HBox {
        setSpacing(5)
      }

      NodeTableCell { tag =>
        hbox.getChildren.setAll(Tags.toNodes(tag.asJava))
        hbox
      }
    })
  }

  override protected def dataBinding(): Unit = {
    tableCV.itemsProperty().bind(loadCVService.valueProperty())
  }

  override protected def eventBinding(): Unit = {
    object Page {
      def openCV(name: String) = {
        HGameApp.loadCVTab(name, true)
      }
    }

    loadCVService.valueProperty().addListener {
      (_, _, list) => {
        if (list != null) {

          val root = new VelocityContext()
          root.put("cvlist", list)

          val str = VelocityTemplateConfig
            .tpl("/tpl/game/cvinfo.html")
            .process(root)


          // set js obj
          val webEngine = cvWebView.getEngine
          webEngine.getLoadWorker.stateProperty.addListener((_, _, newState) => {
            if (newState eq Worker.State.SUCCEEDED) {
              val win = webEngine.executeScript("window").asInstanceOf[JSObject] // 获取js对象
              win.setMember("app", Page) // 然后把应用程序对象设置成为js对象
            }
          })
          webEngine.loadContent(str)
        }

      }
    }

    registestListener(tableCV.itemsProperty())
  }

  override def load() = {
    loadCVService.restart()


  }

}