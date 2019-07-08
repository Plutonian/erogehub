package com.goexp.galgame.gui.view.cv;

import com.goexp.common.util.Strings;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.res.Images;
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

    private Game game;

//    @FXML
//    private CVSearchController cvsearchController;

    @FXML
    private ImageView imageImg;

    @FXML
    private Text txtName;

//    @FXML
//    private MenuButton lbCV;
//
//    @FXML
//    private MenuItem truecv;

    @FXML
    private Text txtIntro;

//    @FXML
//    private Region cvPart;


    public void init() {


        logger.debug("{}", gameChar);

        txtName.setText(gameChar.name);


//        boolean isTrueCV = gameChar.trueCV != null && gameChar.trueCV.length() > 0;

//        var cv = isTrueCV ? "*"+gameChar.trueCV +"*": gameChar.cv;
//
//        if (Strings.isNotEmpty(cv)) {
//            cvPart.setVisible(true);
////            lbCV.setText(cv);
//
////            truecv.setOnAction(event -> {
////                HomeController.$this.loadCVTab(isTrueCV ? gameChar.trueCV : gameChar.cv, isTrueCV);
////            });
//        } else {
//            cvPart.setVisible(false);
//        }


        if (Strings.isEmpty(gameChar.intro)) {
            txtIntro.setVisible(false);
        } else
            txtIntro.setText(gameChar.intro);


        if (Strings.isNotEmpty(gameChar.img )) {

//            imageImg.setImage(Images.GameImage.GameChar.small(game, gameChar.index, gameChar.img));
        } else {
            imageImg.setImage(null);
        }

    }

    @Override
    protected void initialize() {

    }
}