package com.goexp.galgame.gui.view.detailview.header;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.util.LocalRes;
import com.goexp.galgame.gui.util.UIUtil;
import com.goexp.galgame.gui.view.common.StateChangeController;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.detailview.DateShowController;
import com.goexp.galgame.gui.view.detailview.StarChoiceBarController;
import com.goexp.galgame.gui.view.search.MainSearchController;
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

import java.util.regex.Pattern;
import java.util.stream.Collectors;


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

    private static final Pattern NAME_SPLITER_REX = Pattern.compile("[〜\\-＜\\s]");

    @FXML
    private void initialize() {

        flowPainter.addEventFilter(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof Hyperlink) {

                var painter = (Hyperlink) event.getTarget();

                var str = painter.getText().replaceAll("（[^）]+）", "");
                MainSearchController.$this.loadPainterTab(str);
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

        flowPainter.getChildren().setAll(UIUtil.createSetLink(game.painter, label -> {
        }));
        txtWriter.setText(game.writer.stream().collect(Collectors.joining(",")));

        if (game.tag.size() > 0) {
            boxTag.getChildren().setAll(UIUtil.createSet(game.tag, label -> {
                label.getStyleClass().add("tag");
                label.getStyleClass().add("tagbig");
            }));
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
