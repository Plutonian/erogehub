package com.goexp.galgame.gui.view.game.detailview.cell;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.DefaultController;
import com.goexp.galgame.gui.util.Images;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.CVSearchController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;


public class PersonCellController extends DefaultController {
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

            truecv.setOnAction(event -> {
                HomeController.$this.loadCVTab(isTrueCV ? gameChar.trueCV : gameChar.cv, isTrueCV);
            });
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

    @Override
    protected void initialize() {

    }
}
