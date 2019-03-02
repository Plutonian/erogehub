package com.goexp.galgame.gui.view.detailview.cell;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.common.jump.CVSearchController;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.view.search.MainSearchController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameCharListCellController {

    private final Logger logger = LoggerFactory.getLogger(GameCharListCellController.class);
    public Game.GameCharacter gameChar;
    public int gameId;

    @FXML
    private CVSearchController cvsearchController;

    @FXML
    private ImageView imageImg;

    @FXML
    private Text txtName;

    @FXML
    private MenuButton lbCV;

    @FXML
    private MenuItem truecv;

    @FXML
    private Text txtIntro;

    @FXML
    private Region cvPart;


    public void init() {


        logger.debug("{}", gameChar);

        txtName.setText(gameChar.name);


        boolean isTrueCV = gameChar.trueCV != null && gameChar.trueCV.length() > 0;

        var cv = isTrueCV ? gameChar.trueCV : gameChar.cv;

        if (cv != null && cv.length() > 0) {
            cvPart.setVisible(true);
            cvsearchController.load(cv);
            lbCV.setText(cv);

            if (isTrueCV) {
                truecv.setVisible(true);
                truecv.setOnAction(event -> {
                    MainSearchController.$this.loadCVTab(gameChar.trueCV);
                });
            } else {
                truecv.setVisible(false);
                truecv.setOnAction(null);
            }
        } else {
            cvPart.setVisible(false);
        }


        if (gameChar.intro == null || gameChar.intro.trim().length() == 0) {
            txtIntro.setVisible(false);
        } else
            txtIntro.setText(gameChar.intro);


        if (gameChar.img != null && gameChar.img.length() > 0) {

            imageImg.setImage(Images.GameImage.GameChar.small(gameId, gameChar.index, gameChar.img));
        } else {
            imageImg.setImage(null);
        }

    }


}
