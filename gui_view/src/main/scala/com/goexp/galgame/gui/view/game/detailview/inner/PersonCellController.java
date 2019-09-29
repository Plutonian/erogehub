package com.goexp.galgame.gui.view.game.detailview.inner;

import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.res.gameimg.PersonImage;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.HomeController;
import com.goexp.galgame.gui.view.game.detailview.part.CVSearchController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class PersonCellController extends DefaultController {
    Game.GameCharacter gameChar;

    public Game game;

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

        var cv = isTrueCV ? "*" + gameChar.trueCV + "*" : gameChar.cv;

        if (cv != null && cv.length() > 0) {
            cvPart.setVisible(true);
            cvsearchController.load(cv);
            lbCV.setText(cv);

            truecv.setOnAction(event -> HomeController.$this.loadCVTab(isTrueCV ? gameChar.trueCV : gameChar.cv, isTrueCV));
        } else {
            cvPart.setVisible(false);
        }


        if (gameChar.intro == null || gameChar.intro.trim().length() == 0) {
            txtIntro.setVisible(false);
        } else
            txtIntro.setText(gameChar.intro);


        if (gameChar.img != null && gameChar.img.length() > 0) {


            new PersonImage(game).onOK((img) -> {
                imageImg.setImage(img);
                return null;
            }).small(gameChar.index, gameChar.img);


        } else {
            imageImg.setImage(null);
        }

    }

    @Override
    protected void initialize() {

    }
}
