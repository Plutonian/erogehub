package com.goexp.galgame.gui.view.detailview.header;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.view.common.jump.JumpBrandController;
import com.goexp.galgame.gui.view.common.jump.JumpLinkController;
import com.goexp.galgame.gui.view.detailview.StateChangeChoiceBarController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SmallHeaderController {
    private final Logger logger = LoggerFactory.getLogger(SmallHeaderController.class);


    @FXML
    private JumpLinkController webjumpController;

    @FXML
    private JumpBrandController brandJumpController;

    @FXML
    private StateChangeChoiceBarController changeStateController;

    @FXML
    private Text txtName;

    @FXML
    private Label lbDate;

    @FXML
    private HBox boxTag;


    @FXML
    private void initialize() {

    }


    public void load(Game game) {

        webjumpController.load(game);

        txtName.setText(game.name);


        brandJumpController.load(game.brand);

        changeStateController.load(game);


        if (game.tag.size() > 0) {

            boxTag.getChildren().setAll(Tags.toNodes(game.tag, str -> {
                var tagLabel = new Label(str);

                tagLabel.getStyleClass().add("tag");
                tagLabel.getStyleClass().add("tagbig");

                return tagLabel;
            }));
        } else {
            boxTag.getChildren().clear();
        }

        lbDate.setText(DateUtil.formatDate(game.publishDate));

    }
}
