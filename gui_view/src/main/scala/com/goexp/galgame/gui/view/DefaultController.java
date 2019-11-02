package com.goexp.galgame.gui.view;

import com.typesafe.scalalogging.Logger;
import com.typesafe.scalalogging.Logger$;
import javafx.fxml.FXML;

public abstract class DefaultController {

    protected final Logger logger = Logger$.MODULE$.apply(getClass());

    @FXML
    protected abstract void initialize();
}
