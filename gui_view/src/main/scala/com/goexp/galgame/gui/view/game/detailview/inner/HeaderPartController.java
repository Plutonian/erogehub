package com.goexp.galgame.gui.view.game.detailview.inner;

import com.goexp.common.util.string.Strings;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.util.res.gameimg.GameImage;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class HeaderPartController extends DefaultController {

//        @FXML
//        private StarChoiceBarController starChangeController;

//    @FXML
//    private DateShowController dateviewController;

//        @FXML
//        private JumpLinkController webjumpController;

//    @FXML
//    private JumpBrandController brandJumpController;

//        @FXML
//        private StateChangeController changeStateController;

    @FXML
    private ImageView imageImg;

//    @FXML
//    private HBox boxStar;
//
//    @FXML
//    private Text txtName;
//
//    @FXML
//    private Text txtSubName;

    @FXML
    private FlowPane flowPainter;

    //    @FXML//    public Label lbDate;
    @FXML
    private Label txtWriter;

//    @FXML
//    private HBox boxTag;

    @FXML
    private Text txtIntro;

    @FXML
    private TextArea txtStory;

    private Game targetGame;

    protected void initialize() {

        flowPainter.addEventFilter(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof Hyperlink) {

                var painter = (Hyperlink) event.getTarget();

                var str = painter.getText().replaceAll("（[^）]+）", "");
                HomeController.$this().loadPainterTab(str);
            }
        });

//            starChangeController.onStarChangeProperty.addListener((observable, oldValue, changed) -> {
//
//                if (changed) {
//                    loadStar(targetGame);
//                }
//            });
    }


    public void load(Game game) {
        this.targetGame = game;

        loadWithoutImage(game);

        if (game.isOkImg()) {
            imageImg.setImage(new GameImage(game).small());
        } else {
            imageImg.setImage(null);
        }
    }


    public void setImage(Image image) {
        imageImg.setImage(image);
    }

    private void loadWithoutImage(Game game) {
        this.targetGame = game;

//            webjumpController.load(game);
//            changeStateController.load(game);
//        brandJumpController.load(game.brand);
//            starChangeController.load(game);


//        txtName.setText(game.getMainName());
//        txtSubName.setText(game.getSubName());


        flowPainter.getChildren().setAll(Tags.toNodes(game.painter, Hyperlink::new));
        txtWriter.setText(String.join(",", game.writer));

        if (Strings.isNotEmpty(game.intro))
            txtIntro.setText(game.intro + "\n\n");

        txtStory.setText(game.story);

//        if (game.tag.size() > 0) {
//            var nodes = Tags.toNodes(game.tag, str -> {
//                var tagLabel = new Label(str);
//                tagLabel.getStyleClass().add("tag");
//                tagLabel.getStyleClass().add("tagbig");
//
//                return tagLabel;
//            });
//
//            boxTag.getChildren().setAll(nodes);
//        } else {
//            boxTag.getChildren().clear();
//        }

//        dateviewController.load(game.publishDate);

//        loadStar(game);

//            if (game.state.get().value <= GameState.BLOCK.value)
//                imageImg.setEffect(new ColorAdjust(0, -1, 0, 0));
//            else
//                imageImg.setEffect(null);

    }

//    public void loadStar(Game game) {
//        boxStar.getChildren().clear();
//        var image = LocalRes.HEART_32_PNG.get();
//        for (var i = 0; i < game.star; i++) {
//            boxStar.getChildren().add(new ImageView(image));
//        }
//    }
}
