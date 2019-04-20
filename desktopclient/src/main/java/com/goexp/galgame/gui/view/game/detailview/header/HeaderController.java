package com.goexp.galgame.gui.view.game.detailview.header;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.util.LocalRes;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.view.game.part.StateChangeController;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.DateShowController;
import com.goexp.galgame.gui.view.game.detailview.part.StarChoiceBarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static com.goexp.galgame.common.util.GameName.NAME_SPLITER_REX;


public class HeaderController {
    private final Logger logger = LoggerFactory.getLogger(HeaderController.class);


    @FXML
    private StarChoiceBarController starChangeController;

    @FXML
    private DateShowController dateviewController;

    @FXML
    private JumpLinkController webjumpController;

    @FXML
    private JumpBrandController brandJumpController;

    @FXML
    private StateChangeController changeStateController;

    @FXML
    private ImageView imageImg;

    @FXML
    private HBox boxStar;

    @FXML
    private Text txtName;

    @FXML
    private Text txtSubName;

    @FXML
    private FlowPane flowPainter;

    //    @FXML
//    public Label lbDate;
    @FXML
    private Label txtWriter;

    @FXML
    private HBox boxTag;

    private Game targetGame;

    @FXML
    private void initialize() {

        flowPainter.addEventFilter(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof Hyperlink) {

                var painter = (Hyperlink) event.getTarget();

                var str = painter.getText().replaceAll("（[^）]+）", "");
                HomeController.$this.loadPainterTab(str);
            }
        });

        starChangeController.onStarChangeProperty.addListener((observable, oldValue, changed) -> {

            if (changed) {
                loadStar(targetGame);
            }
        });
    }


    public void load(Game game) {
        this.targetGame = game;

        loadWithoutImage(game);

        if (game.smallImg != null && game.smallImg.startsWith("http")) {
            imageImg.setImage(Images.GameImage.small(game));

        } else {
            imageImg.setImage(null);
        }
    }


    public void setImage(Image image) {
        imageImg.setImage(image);
    }

    public void loadWithoutImage(Game game) {
        this.targetGame = game;

        webjumpController.load(game);
        changeStateController.load(game);
        brandJumpController.load(game.brand);
        starChangeController.load(game);

        var matcher = NAME_SPLITER_REX.matcher(game.name);
        final var find = matcher.find();


        txtName.setText(find ? game.name.substring(0, matcher.start()) : game.name);
        txtSubName.setText(find ? game.name.substring(matcher.start()) : "");

        flowPainter.getChildren().setAll(Tags.toNodes(game.painter, str1 -> {
            var tagLabel1 = new Hyperlink(str1);

            return tagLabel1;
        }));
        txtWriter.setText(game.writer.stream().collect(Collectors.joining(",")));

        if (game.tag.size() > 0) {
            var nodes = Tags.toNodes(game.tag, str -> {
                var tagLabel = new Label(str);
                tagLabel.getStyleClass().add("tag");
                tagLabel.getStyleClass().add("tagbig");

                return tagLabel;
            });

            boxTag.getChildren().setAll(nodes);
        } else {
            boxTag.getChildren().clear();
        }

        dateviewController.load(game.publishDate);

        loadStar(game);

        if (game.state.get().getValue() <= GameState.BLOCK.getValue())
            imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
        else
            imageImg.setEffect(null);

    }

    public void loadStar(Game game) {
        boxStar.getChildren().clear();
        var image = LocalRes.HEART_32_PNG.get();
        for (var i = 0; i < game.star; i++) {
            boxStar.getChildren().add(new ImageView(image));
        }
    }
}
